package pe.gob.pj.prueba.infraestructure.rest.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.servicio.PerfilUsuario;
import pe.gob.pj.prueba.domain.port.usecase.AccesoUseCasePort;
import pe.gob.pj.prueba.domain.utils.CaptchaUtils;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.enums.Claim;
import pe.gob.pj.prueba.infraestructure.enums.Estado;
import pe.gob.pj.prueba.infraestructure.enums.FormatoRespuesta;
import pe.gob.pj.prueba.infraestructure.rest.request.LoginRequest;
import pe.gob.pj.prueba.infraestructure.rest.request.ObtenerOpcionesRequest;
import pe.gob.pj.prueba.infraestructure.rest.response.PerfilOpcionesResponse;
import pe.gob.pj.prueba.infraestructure.rest.response.UsuarioResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccesoController implements Acceso {

  @Qualifier("accesoUseCasePort")
  final AccesoUseCasePort accesoUC;

  @Override
  public ResponseEntity<UsuarioResponse> iniciarSesion(String cuo, String ip, String jwt,
      @Valid LoginRequest request) {

    var res = new UsuarioResponse();
    res.setCodigoOperacion(cuo);
    try {
      if (!request.getAplicaCaptcha().equalsIgnoreCase(Estado.ACTIVO_LETRA.getNombre())
          || (request.getAplicaCaptcha().equalsIgnoreCase(Estado.ACTIVO_LETRA.getNombre())
              && !ProjectUtils.isNullOrEmpty(request.getTokenCaptcha()))) {
        if (!request.getAplicaCaptcha().equalsIgnoreCase(Estado.ACTIVO_LETRA.getNombre())
            || CaptchaUtils.validCaptcha(request.getTokenCaptcha(), ip, cuo)) {
          var usuario = accesoUC.iniciarSesion(cuo, request.getUsuario(), request.getClave());
          var rolesUsuario = usuario.getPerfiles().stream().map(PerfilUsuario::getRol).toList();

          var token = getNewToken(jwt, request.getUsuario(), rolesUsuario, ip, cuo);
          if (!ProjectUtils.isNullOrEmpty(token)) {
            usuario.setToken(token);
            res.setData(usuario);
          } else {
            res.setCodigo(Errors.ERROR_TOKEN_NO_VALIDO.getCodigo());
            res.setDescripcion(Errors.ERROR_TOKEN_NO_VALIDO.getNombre());
          }
        } else {
          log.error(
              "{} Datos de validación captcha -> indicador de validación: {}, token captcha: {} y la ip de la petición",
              cuo, request.getAplicaCaptcha(), request.getTokenCaptcha(), ip);
          throw new ErrorException(Errors.ERROR_TOKEN_CAPTCHA.getCodigo(), String
              .format(Errors.ERROR_TOKEN_CAPTCHA.getNombre(), Proceso.INICIAR_SESION.getNombre()));
        }
      } else {
        log.error(
            "{} Datos de validación captcha -> indicador de validación: {}, token captcha: {} y la ip de la petición",
            cuo, request.getAplicaCaptcha(), request.getTokenCaptcha(), ip);
        throw new ErrorException(Errors.ERROR_TOKEN_CAPTCHA.getCodigo(), String
            .format(Errors.ERROR_TOKEN_CAPTCHA.getNombre(), Proceso.INICIAR_SESION.getNombre()));
      }
    } catch (ErrorException e) {
      res.setCodigo(e.getCodigo());
      res.setDescripcion(e.getDescripcion());
      handleException(cuo, e);
    } catch (Exception e) {
      res.errorInesperado(Proceso.INICIAR_SESION.getNombre());
      handleException(cuo,
          new ErrorException(res.getCodigo(), res.getDescripcion(), e.getMessage(), e.getCause()));
    }
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(
        FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta())
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PerfilOpcionesResponse> obtenerOpciones(String cuo, String ip, String jwt,
      @Valid ObtenerOpcionesRequest request) {
    var res = new PerfilOpcionesResponse();
    res.setCodigoOperacion(cuo);
    try {

      var perfilOpciones = accesoUC.obtenerOpciones(cuo, request.getIdPerfil());

      var rolesPerfil = Arrays.asList(perfilOpciones.getRol());
      var token = getNewToken(jwt, request.getUsuario(), rolesPerfil, ip, cuo);
      if (!ProjectUtils.isNullOrEmpty(token)) {
        perfilOpciones.setToken(token);
        res.setData(perfilOpciones);
      } else {
        res.setCodigo(Errors.ERROR_TOKEN_NO_VALIDO.getCodigo());
        res.setDescripcion(Errors.ERROR_TOKEN_NO_VALIDO.getNombre());
      }
    } catch (ErrorException e) {
      res.setCodigo(e.getCodigo());
      res.setDescripcion(e.getDescripcion());
      handleException(cuo, e);
    } catch (Exception e) {
      res.errorInesperado(Proceso.ELEGIR_PERFIL.getNombre());
      handleException(cuo,
          new ErrorException(res.getCodigo(), res.getDescripcion(), e.getMessage(), e.getCause()));

    }
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(
        FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta())
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  private String getNewToken(String token, String usuario, List<String> rolesUsuario,
      String ipRemota, String cuo) {
    var newToken = "";
    var rolSeleccionado = new StringBuilder();
    try {
      var signingKey = SecurityConstants.JWT_SECRET.getBytes();
      try {
        Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKey))
            .build().parseClaimsJws(token);

        @SuppressWarnings("unchecked")
        List<String> rolesToken = (List<String>) parsedToken.getBody().get(Claim.ROLES.getNombre());

        rolSeleccionado.append(rolesUsuario.get(0));
        if (rolesToken.stream().filter(x -> x.equals(rolSeleccionado.toString())).count() < 1)
          return newToken;
        else
          rolesToken = rolesUsuario;


        var ipRemotaToken =
            parsedToken.getBody().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
        var subject = parsedToken.getBody().getSubject();

        var ahora = new Date();

        var tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
        var tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();

        if (ipRemota.equals(ipRemotaToken) && !rolesToken.isEmpty()) {
          newToken =
              Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                  .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                  .setIssuer(SecurityConstants.TOKEN_ISSUER)
                  .setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(subject)
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
          newToken =
              Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                  .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                  .setIssuer(SecurityConstants.TOKEN_ISSUER)
                  .setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(subject)
                  .setExpiration(new Date(System.currentTimeMillis() + tiempoToken))
                  .claim(Claim.ROLES.getNombre(), roles)
                  .claim(Claim.ROL_SELECCIONADO.getNombre(), rolSeleccionado.toString())
                  .claim(Claim.USUARIO_REALIZA_PETICION.getNombre(), usuario)
                  .claim(Claim.IP_REALIZA_PETICION.getNombre(), ipRemota).compact();
        }
      }
    } catch (Exception e) {
      log.error("{} error al intentar generar nuevo Token: {}", cuo,
          ProjectUtils.isNull(e.getCause()).concat(e.getMessage()));
    }
    return newToken;
  }

}
