package pe.gob.pj.prueba.infraestructure.rest.apis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.exceptions.TokenException;
import pe.gob.pj.prueba.domain.model.Aplicativo;
import pe.gob.pj.prueba.domain.model.AplicativoToken;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.common.enums.Claim;
import pe.gob.pj.prueba.infraestructure.common.enums.FormatoRespuesta;
import pe.gob.pj.prueba.infraestructure.common.enums.TipoError;
import pe.gob.pj.prueba.infraestructure.rest.responses.AplicativoResponse;
import pe.gob.pj.prueba.infraestructure.rest.responses.AplicativoTokenResponse;

@Slf4j
@RestController
public class DefaultController implements Default, Serializable {

  private static final long serialVersionUID = 1L;

  @GetMapping(value = "/healthcheck")
  public ResponseEntity<AplicativoResponse> healthcheck(String cuo, String formatoRespuesta) {
    var res = new AplicativoResponse();

    res.setCodigoOperacion(cuo);
    var healthcheck = new Aplicativo();
    healthcheck.setNombre(ProjectConstants.Aplicativo.NOMBRE);
    healthcheck.setEstado("Disponible");
    healthcheck.setVersion(ProjectConstants.Aplicativo.VERSION);
    res.setData(healthcheck);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType
        .parseMediaType(FormatoRespuesta.JSON.getNombre().equalsIgnoreCase(formatoRespuesta)
            ? MediaType.APPLICATION_JSON_VALUE
            : MediaType.APPLICATION_XML_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);

  }

  @SuppressWarnings("unchecked")
  @GetMapping(value = "/seguridad/refresh")
  public ResponseEntity<AplicativoTokenResponse> refreshToken(String cuo, String ipRemota,
      String token) {
    var res = new AplicativoTokenResponse();
    res.setCodigoOperacion(cuo);

    byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

    var dataToken = new AplicativoToken();
    try {
      Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(signingKey))
          .build().parseClaimsJws(token);
      List<String> roles = (List<String>) parsedToken.getBody().get(Claim.ROLES.getNombre());
      var ipRemotaToken =
          parsedToken.getBody().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
      var rolSeleccionado =
          parsedToken.getBody().get(Claim.ROL_SELECCIONADO.getNombre()).toString();
      var usuario =
          parsedToken.getBody().get(Claim.USUARIO_REALIZA_PETICION.getNombre()).toString();
      var subject = parsedToken.getBody().getSubject();

      var ahora = new Date();

      int tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
      int tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();

      Date limiteExpira = parsedToken.getBody().getExpiration();
      Date limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);

      if (ipRemota.equals(ipRemotaToken)) {
        if (!ahora.after(limiteRefresh)) {
          var tokenResult =
              Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                  .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                  .setIssuer(SecurityConstants.TOKEN_ISSUER)
                  .setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(subject)
                  .setExpiration(ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira))
                  .claim(Claim.ROLES.getNombre(), roles)
                  .claim(Claim.ROL_SELECCIONADO.getNombre(), rolSeleccionado)
                  .claim(Claim.USUARIO_REALIZA_PETICION.getNombre(), usuario)
                  .claim(Claim.IP_REALIZA_PETICION.getNombre(), ipRemota)
                  .claim(Claim.LIMITE_TOKEN.getNombre(), ProjectUtils.sumarRestarSegundos(ahora,
                      tiempoSegundosExpira + tiempoSegundosRefresh))
                  .compact();
          dataToken.setToken(tokenResult);
          res.setData(dataToken);
          return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
          log.error(
              "{} El tiempo límite para refrescar el token enviado a expirado actual [{}] limite [{}].",
              cuo, ahora, limiteRefresh);
          throw new TokenException();
        }
      } else {
        log.error(
            "{} La ip del token origen [{}] no coincide con la ip de la peticion actual [{}].", cuo,
            ipRemotaToken, ipRemota);
        throw new TokenException();
      }
    } catch (ExpiredJwtException e) {
      List<String> roles = (List<String>) e.getClaims().get(Claim.ROLES.getNombre());
      var rolSeleccionado = e.getClaims().get(Claim.ROL_SELECCIONADO.getNombre()).toString();
      var usuario = e.getClaims().get(Claim.USUARIO_REALIZA_PETICION.getNombre()).toString();
      var ipRemotaToken = e.getClaims().get(Claim.IP_REALIZA_PETICION.getNombre()).toString();
      var subject = e.getClaims().getSubject();

      var ahora = new Date();

      var tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
      var tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();

      var limiteExpira = e.getClaims().getExpiration();
      var limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);

      if (ipRemota.equals(ipRemotaToken)) {
        if (!ahora.after(limiteRefresh)) {
          String tokenResult =
              Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                  .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                  .setIssuer(SecurityConstants.TOKEN_ISSUER)
                  .setAudience(SecurityConstants.TOKEN_AUDIENCE).setSubject(subject)
                  .setExpiration(ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira))
                  .claim(Claim.ROLES.getNombre(), roles)
                  .claim(Claim.ROL_SELECCIONADO.getNombre(), rolSeleccionado)
                  .claim(Claim.USUARIO_REALIZA_PETICION.getNombre(), usuario)
                  .claim(Claim.IP_REALIZA_PETICION.getNombre(), ipRemota)
                  .claim(Claim.LIMITE_TOKEN.getNombre(), ProjectUtils.sumarRestarSegundos(ahora,
                      tiempoSegundosExpira + tiempoSegundosRefresh))
                  .compact();
          res.setCodigo(TipoError.OPERACION_EXITOSA.getCodigo());
          res.setDescripcion(TipoError.OPERACION_EXITOSA.getDescripcion());
          dataToken.setToken(tokenResult);
          res.setData(dataToken);
          return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
          log.error(
              "{} El tiempo límite para refrescar el token enviado a expirado actual [{}] limite [{}].",
              cuo, ahora, limiteRefresh);
          throw new TokenException();
        }
      } else {
        log.error(
            "{} La ip del token origen [{}] no coincide con la ip de la peticion actual [{}].", cuo,
            ipRemotaToken, ipRemota);
        throw new TokenException();
      }
    }
  }

}
