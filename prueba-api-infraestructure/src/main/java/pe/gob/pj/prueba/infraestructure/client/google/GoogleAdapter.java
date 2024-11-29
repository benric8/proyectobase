package pe.gob.pj.prueba.infraestructure.client.google;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.common.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.common.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.exceptions.CaptchaException;
import pe.gob.pj.prueba.domain.model.client.google.response.CaptchaValid;
import pe.gob.pj.prueba.domain.port.client.google.GooglePort;

@Service("googlePort")
@Slf4j
public class GoogleAdapter implements GooglePort {

  @Override
  public boolean validarCaptcha(String cuo, String token, String remoteIp) {
    boolean resultado = Boolean.FALSE;
    UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(ProjectProperties.getCaptchaUrl())
        .queryParam("secret", ProjectProperties.getCaptchaToken()).queryParam("response", token)
        .queryParam("remoteip", remoteIp).build();
    try {
      RestTemplate plantilla = new RestTemplate();
      var captcha = plantilla.postForObject(uriBuilder.toUriString(), null, CaptchaValid.class);
      if (captcha.getSuccess().equals("true")) {
        return Boolean.TRUE;
      }
    } catch (Exception e) {
      log.info("{} Error al consumir google : {} ", cuo, uriBuilder.toUriString());
      log.error("{} {}", cuo, ProjectUtils.convertExceptionToString(e));
      throw new CaptchaException();
    }
    return resultado;
  }

}
