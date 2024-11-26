package pe.gob.pj.prueba.usecase;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.Cipher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.exceptions.CaptchaException;
import pe.gob.pj.prueba.domain.exceptions.CredencialesSinCoincidenciaException;
import pe.gob.pj.prueba.domain.exceptions.OpcionesNoAsignadadException;
import pe.gob.pj.prueba.domain.exceptions.TokenException;
import pe.gob.pj.prueba.domain.exceptions.UsuarioSinPerfilAsignadoException;
import pe.gob.pj.prueba.domain.model.auditoriageneral.PeticionServicios;
import pe.gob.pj.prueba.domain.model.common.enums.Claim;
import pe.gob.pj.prueba.domain.model.common.enums.Flag;
import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.servicio.PerfilUsuario;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;
import pe.gob.pj.prueba.domain.model.servicio.query.IniciarSesionQuery;
import pe.gob.pj.prueba.domain.port.client.google.GooglePort;
import pe.gob.pj.prueba.domain.port.persistence.AccesoPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.AccesoUseCasePort;
import pe.gob.pj.prueba.domain.utils.EncryptUtils;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;

@Service("accesoUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AccesoUseCaseAdapter implements AccesoUseCasePort {

  final AccesoPersistencePort accesoPersistencePort;
  final GooglePort googlePort;

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public Usuario iniciarSesion(String cuo, IniciarSesionQuery iniciarSesionQuery,
      PeticionServicios peticion) {

    if (Flag.SI.getCodigo().equalsIgnoreCase(
        Optional.ofNullable(iniciarSesionQuery.aplicaCaptcha()).map(String::trim).orElse(null))
        && ProjectUtils.isNullOrEmpty(iniciarSesionQuery.tokenCaptcha())) {
      log.error(
          "{} Datos de validación captcha -> indicador de validación: {}, token captcha: {} y la ip de la petición {}",
          peticion.getCuo(), iniciarSesionQuery.aplicaCaptcha(), iniciarSesionQuery.tokenCaptcha(),
          peticion.getIp());
      throw new CaptchaException();
    }

    if (Flag.SI.getCodigo().equalsIgnoreCase(
        Optional.ofNullable(iniciarSesionQuery.aplicaCaptcha()).map(String::trim).orElse(null))
        && !googlePort.validarCaptcha(iniciarSesionQuery.tokenCaptcha(), peticion.getIp(),
            peticion.getCuo())) {
      log.error(
          "{} Datos de validación captcha -> indicador de validación: {}, token captcha: {} y la ip de la petición {}",
          peticion.getCuo(), iniciarSesionQuery.aplicaCaptcha(), iniciarSesionQuery.tokenCaptcha(),
          peticion.getIp());
      throw new CaptchaException();
    }

    var user = accesoPersistencePort.iniciarSesion(cuo, iniciarSesionQuery.usuario());
    var password = EncryptUtils.cryptBase64u(iniciarSesionQuery.clave(), Cipher.ENCRYPT_MODE);

    if (Objects.isNull(user) || user.getClave().isBlank() || !user.getClave().equals(password)) {
      throw new CredencialesSinCoincidenciaException();
    }

    if (user.getPerfiles().isEmpty()) {
      throw new UsuarioSinPerfilAsignadoException();
    }

    user.setClave("******");
    user.setToken(
        generarNuevoToken(peticion.getCuo(), peticion.getJwt(), iniciarSesionQuery.usuario(),
            user.getPerfiles().stream().map(PerfilUsuario::getRol).toList(), peticion.getIp()));

    return user;

  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public PerfilOpcions obtenerOpciones(String cuo, String usuario, Integer idPerfil,
      PeticionServicios peticion) {
    var perfilOpciones = accesoPersistencePort.obtenerOpciones(cuo, idPerfil);
    if (perfilOpciones.getOpciones().isEmpty()) {
      throw new OpcionesNoAsignadadException();
    }
    perfilOpciones.setToken(generarNuevoToken(peticion.getCuo(), peticion.getJwt(), usuario,
        Arrays.asList(perfilOpciones.getRol()), peticion.getIp()));
    return perfilOpciones;
  }

  private String generarNuevoToken(String cuo, String token, String usuario,
      List<String> rolesUsuario, String ipRemota) {
    var newToken = "";
    var rolSeleccionado = new StringBuilder();
    var signingKey = SecurityConstants.JWT_SECRET.getBytes();
    try {
      Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKey))
          .build().parseClaimsJws(token);

      @SuppressWarnings("unchecked")
      List<String> rolesToken = (List<String>) parsedToken.getBody().get(Claim.ROLES.getNombre());

      rolSeleccionado.append(rolesUsuario.get(0));
      if (rolesToken.stream().filter(x -> x.equals(rolSeleccionado.toString())).count() < 1) {
        log.error(
            "{} Error al generar nuevo token, el rol seleccionado [{}] no es un rol asignado al usuario [{}].",
            cuo, rolSeleccionado, usuario);
        throw new TokenException();
      }
      rolesToken = rolesUsuario;

      var ipRemotaToken =
          parsedToken.getBody().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
      var subject = parsedToken.getBody().getSubject();
      var ahora = new Date();
      var tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
      var tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();

      if (ipRemota.equals(ipRemotaToken) && !rolesToken.isEmpty()) {
        newToken = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
            .setIssuer(SecurityConstants.TOKEN_ISSUER).setAudience(SecurityConstants.TOKEN_AUDIENCE)
            .setSubject(subject)
            .setExpiration(ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira))
            .claim(Claim.ROLES.getNombre(), rolesToken)
            .claim(Claim.ROL_SELECCIONADO.getNombre(), rolSeleccionado.toString())
            .claim(Claim.USUARIO_REALIZA_PETICION.getNombre(), usuario)
            .claim(Claim.IP_REALIZA_PETICION.getNombre(), ipRemota)
            .claim(Claim.LIMITE_TOKEN.getNombre(), ProjectUtils.sumarRestarSegundos(ahora,
                tiempoSegundosExpira + tiempoSegundosRefresh))
            .compact();
      }
    } catch (ExpiredJwtException e) {
      var roles = rolesUsuario;
      var ipRemotaToken = e.getClaims().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
      var subject = e.getClaims().getSubject();
      var tiempoToken = ProjectProperties.getSeguridadTiempoExpiraSegundos();
      if (ipRemota.equals(ipRemotaToken)) {
        newToken = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
            .setIssuer(SecurityConstants.TOKEN_ISSUER).setAudience(SecurityConstants.TOKEN_AUDIENCE)
            .setSubject(subject).setExpiration(new Date(System.currentTimeMillis() + tiempoToken))
            .claim(Claim.ROLES.getNombre(), roles)
            .claim(Claim.ROL_SELECCIONADO.getNombre(), rolSeleccionado.toString())
            .claim(Claim.USUARIO_REALIZA_PETICION.getNombre(), usuario)
            .claim(Claim.IP_REALIZA_PETICION.getNombre(), ipRemota).compact();
      }
    }
    return newToken;
  }

}
