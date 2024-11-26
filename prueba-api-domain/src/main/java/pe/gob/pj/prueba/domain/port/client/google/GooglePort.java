package pe.gob.pj.prueba.domain.port.client.google;

public interface GooglePort {

  boolean validarCaptcha(String cuo, String token, String remoteIp);

}
