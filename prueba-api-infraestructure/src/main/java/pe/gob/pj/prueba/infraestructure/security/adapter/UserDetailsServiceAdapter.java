package pe.gob.pj.prueba.infraestructure.security.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.seguridad.RoleSecurity;
import pe.gob.pj.prueba.domain.model.seguridad.UserSecurity;
import pe.gob.pj.prueba.domain.port.usecase.SeguridadUseCasePort;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;

@Slf4j
@Service
public class UserDetailsServiceAdapter implements UserDetailsService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private SeguridadUseCasePort service;

  public UserDetailsServiceAdapter(SeguridadUseCasePort service, PasswordEncoder passwordEncoder) {
    super();
    this.service = service;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    var user = new UserSecurity();
    try {
      var u = service.recuperaInfoUsuario("", username);
      if (u != null && u.getId() > 0) {
        user.setId(u.getId());
        user.setName(u.getCUsuario());
        user.setPassword(passwordEncoder.encode(u.getCClave()));
        var roles = new ArrayList<RoleSecurity>();
        var rolesB = service.recuperarRoles("", username);
        rolesB.forEach(rol -> {
          roles.add(new RoleSecurity(rol.id, rol.getCRol()));
        });
        user.setRoles(roles);
      } else {
        throw new Exception("Usuario con ID:  " + username + " not found");
      }
    } catch (Exception e) {
      log.debug("ERROR AL RECUPERAR USUARIO Y ROLES PARA SPRING SECURITY: "
          + ProjectUtils.convertExceptionToString(e));
      e.printStackTrace();
      new UsernameNotFoundException("Usuario con ID:  " + username + " not found");
    }

    return new User(user.getName(), user.getPassword(), getAuthorities(user));
  }

  private static Collection<? extends GrantedAuthority> getAuthorities(UserSecurity user) {
    String[] userRoles =
        user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
    Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
    return authorities;
  }

}
