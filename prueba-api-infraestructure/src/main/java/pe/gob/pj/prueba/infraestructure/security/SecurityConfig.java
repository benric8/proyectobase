package pe.gob.pj.prueba.infraestructure.security;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.common.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.port.usecase.SeguridadUseCasePort;
import pe.gob.pj.prueba.infraestructure.security.adapters.UserDetailsServiceAdapter;
import pe.gob.pj.prueba.infraestructure.security.filters.JwtAuthenticationFilter;
import pe.gob.pj.prueba.infraestructure.security.filters.JwtAuthorizationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

  private final SeguridadUseCasePort seguridadService;

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration
        .setAllowedOrigins(Arrays.asList(ProjectProperties.getSeguridadDominiosPermitidos()));
    configuration.setAllowedMethods(Arrays.asList(HttpMethod.OPTIONS.name(), HttpMethod.GET.name(),
        HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type",
        "X-Requested-With", "Access-Control-Allow-Origin", "codigoCliente", "codigoRol", "username",
        "password", "usucliente", "ipcliente", "pccliente", "maccliente"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    String[] permitidos = ProjectProperties.getSeguridadDominiosPermitidos();
    log.info("Servicio denominado prueba-ws y tiene los siguientes dominios permitidos {} => {}",
        permitidos.length, Arrays.toString(permitidos));
    return source;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.headers(headers -> headers.frameOptions(FrameOptionsConfig::deny))
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/healthcheck").permitAll().anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager(), seguridadService))
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), seguridadService))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  DaoAuthenticationProvider authenticationProvider() {
    UserDetailsServiceAdapter userDetailsService =
        new UserDetailsServiceAdapter(seguridadService, passwordEncoder());
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  AuthenticationManager authenticationManager() {
    return new ProviderManager(authenticationProvider());
  }
}
