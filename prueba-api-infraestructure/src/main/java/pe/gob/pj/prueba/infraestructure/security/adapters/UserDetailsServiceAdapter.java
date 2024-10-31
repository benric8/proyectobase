package pe.gob.pj.prueba.infraestructure.security.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.seguridad.RoleSecurity;
import pe.gob.pj.prueba.domain.model.seguridad.UserSecurity;
import pe.gob.pj.prueba.domain.port.usecase.SeguridadUseCasePort;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsServiceAdapter implements UserDetailsService {

  final SeguridadUseCasePort service;
  final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = new UserSecurity();
    var u = service.recuperaInfoUsuario("", username);
    if (Objects.isNull(u) || u.id() < 1) {
      log.error("No se pudo recuperar la información del usuario [{}].", username);
    }
    user.setId(u.id());
    user.setName(u.cUsuario());
    user.setPassword(passwordEncoder.encode(u.cClave()));
    var roles = new ArrayList<RoleSecurity>();
    var rolesB = service.recuperarRoles("", username);
    rolesB.forEach(rol -> roles.add(new RoleSecurity(rol.id, rol.getCRol())));
    user.setRoles(roles);
    return new User(user.getName(), user.getPassword(), getAuthorities(user));
  }

  private static Collection<? extends GrantedAuthority> getAuthorities(UserSecurity user) {
    String[] userRoles = user.getRoles().stream().map(RoleSecurity::getName).toArray(String[]::new);
    return AuthorityUtils.createAuthorityList(userRoles);
  }

}
