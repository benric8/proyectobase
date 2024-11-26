package pe.gob.pj.prueba.infraestructure.client.google;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.exceptions.CaptchaException;
import pe.gob.pj.prueba.domain.model.client.google.response.CaptchaValid;
import pe.gob.pj.prueba.domain.port.client.google.GooglePort;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;

@Service("googlePort")
@Slf4j
public class GoogleAdapter implements GooglePort {

  @Override
  public boolean validarCaptcha(String cuo, String token, String remoteIp) {
    boolean resultado = Boolean.FALSE;
    try {
      RestTemplate plantilla = new RestTemplate();
      UriComponents builder = UriComponentsBuilder.fromHttpUrl(ProjectProperties.getCaptchaUrl())
          .queryParam("secret", ProjectProperties.getCaptchaToken())
          .queryParam("response", token)
          .queryParam("remoteip", remoteIp)
          .build();
      var captcha = plantilla.postForObject(builder.toUriString(), null, CaptchaValid.class);
      log.info("{} {} : {}", cuo, builder.toUriString(), captcha.getSuccess());
      if (captcha.getSuccess().equals("true")) {
        return Boolean.TRUE;
      }
    } catch (Exception e) {
      log.error("{} {}", cuo, ProjectUtils.convertExceptionToString(e));
      throw new CaptchaException();
    }
    return resultado;
  }

}
