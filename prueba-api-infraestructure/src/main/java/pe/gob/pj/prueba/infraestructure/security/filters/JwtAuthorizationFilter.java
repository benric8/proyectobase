package pe.gob.pj.prueba.infraestructure.security.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.exceptions.TokenException;
import pe.gob.pj.prueba.domain.port.usecase.SeguridadUseCasePort;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.common.enums.Claim;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  @Getter
  @Setter
  private SeguridadUseCasePort seguridadService;
  private final List<String> permittedPaths =
      Arrays.asList("/healthcheck", "/swagger-ui", "/v3/api-docs", "/swagger-ui.html");

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
      SeguridadUseCasePort servicio) {
    super(authenticationManager);
    this.setSeguridadService(servicio);
  }


  /**
   * Descripción : filtra las peticiones HTTP y evalua el token
   * 
   * @param HttpServletRequest request - peticion HTTP
   * @param HttpServletResponse response, - respuesta HTTP
   * @param FilterChain filterChain - cadenas filtro
   * @return void
   * @exception Captura excepcion generica
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    agregarAtributos(request);

    if (isPermittedPath(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
    if (authentication == null) {
      throw new TokenException();
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);

  }

  private boolean isPermittedPath(HttpServletRequest request) {
    var path = request.getRequestURI();
    return permittedPaths.stream().anyMatch(path::contains);
  }

  private void agregarAtributos(HttpServletRequest request) {
    request.setAttribute(ProjectConstants.AUD_CUO, ProjectUtils.obtenerCodigoUnico());
    request.setAttribute(ProjectConstants.AUD_IP,
        !ProjectUtils.isNullOrEmpty(request.getRemoteAddr()) ? request.getRemoteAddr()
            : request.getRemoteHost());
    request.setAttribute(ProjectConstants.AUD_USUARIO, ProjectConstants.Caracter.VACIO);
    request.setAttribute(ProjectConstants.AUD_URI, request.getRequestURI());
    request.setAttribute(ProjectConstants.AUD_PARAMS,
        Objects.nonNull(request.getQueryString()) ? request.getQueryString()
            : ProjectConstants.Caracter.VACIO);
    request.setAttribute(ProjectConstants.AUD_HERRAMIENTA, request.getHeader("User-Agent"));
    request.setAttribute(ProjectConstants.AUD_IPS, obtenerIps(request));
  }

  private String obtenerIps(HttpServletRequest request) {
    String remoteIp = request.getRemoteAddr();
    var headers = Arrays.asList("X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
        "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");
    return Stream
        .concat(Stream.of(remoteIp), headers.stream().map(request::getHeader)
            .filter(Objects::nonNull).distinct().filter(ip -> !ip.equals(remoteIp)))
        .collect(Collectors.joining("|"));
  }

  /**
   * Descripción : obtiene la autenticacion desde token
   * 
   * @param HttpServletRequest request - peticion HTTP
   * @return UsernamePasswordAuthenticationToken - Informacion de autenticacion proveniente token
   * @exception Captura excepcion generica
   */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

    String urlReq = request.getRequestURI();
    String metodo = request.getMethod();
    if (metodo.equalsIgnoreCase(ProjectConstants.METHOD_CORTA_ULTIMA_BARRA_INVERTIDA)) {
      urlReq = urlReq.substring(0, urlReq.lastIndexOf("/"));// corta el id que se manda en la url
    }
    String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
    String remoteIp = request.getAttribute(ProjectConstants.AUD_IP).toString();
    String cuo = request.getAttribute(ProjectConstants.AUD_CUO).toString();
    byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();
    if (!ProjectUtils.isNullOrEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      try {
        String jwt = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        request.setAttribute(ProjectConstants.AUD_JWT, jwt);
        Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKey))
            .build().parseClaimsJws(jwt);

        String username = parsedToken.getBody().getSubject();
        request.setAttribute(ProjectConstants.AUD_USUARIO, username);

        String rolSeleccionado =
            (String) parsedToken.getBody().get(Claim.ROL_SELECCIONADO.getNombre());

        List<SimpleGrantedAuthority> authorities =
            ((List<?>) parsedToken.getBody().get(Claim.ROLES.getNombre())).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority)).toList();

        String ipRemotaDeToken =
            parsedToken.getBody().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();

        int tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();

        Date ahora = new Date();
        Date limiteExpira = parsedToken.getBody().getExpiration();
        Date limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);

        if (!urlReq.endsWith("refresh") && ProjectUtils.isNullOrEmpty(
            seguridadService.validarAccesoMetodo(cuo, username, rolSeleccionado, urlReq))) {
          log.warn("{} El usuario [{}] con rol [{}], no tiene acceso al método [{}] ", cuo,
              username, rolSeleccionado, urlReq);
          return null;
        }

        if (!remoteIp.equals(ipRemotaDeToken)) {
          log.warn(
              "{} La ip que generó el token {} no coincide con la ip desde donde se consume el método {}.",
              cuo, ipRemotaDeToken, remoteIp);
          return null;
        }

        if (ahora.after(limiteRefresh)) {
          log.warn("{} El token [{}] a superado tiempo de expiración [{}] y refresh [{}].", cuo,
              ahora, limiteExpira, limiteRefresh);
          return null;
        }

        if (!ProjectUtils.isNullOrEmpty(username)) {
          return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }

      } catch (ExpiredJwtException exception) {
        String ipRemotaToken =
            exception.getClaims().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
        String subject = exception.getClaims().getSubject();

        if (urlReq.endsWith("refresh") && remoteIp.equals(ipRemotaToken)) {
          List<SimpleGrantedAuthority> authorities =
              ((List<?>) exception.getClaims().get(Claim.ROLES.getNombre())).stream()
                  .map(authority -> new SimpleGrantedAuthority((String) authority)).toList();
          return new UsernamePasswordAuthenticationToken(subject, null, authorities);
        }
        log.warn("{} Request to parse expired JWT : {} failed : {}", cuo, exception.getMessage());
      } 
    }
    return null;
  }

}
