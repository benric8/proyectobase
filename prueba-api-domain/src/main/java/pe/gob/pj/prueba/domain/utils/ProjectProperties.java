package pe.gob.pj.prueba.domain.utils;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(value= {
		@PropertySource("classpath:prueba-api.properties")
})
public class ProjectProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String seguridadSecretToken;
    private static Integer seguridadIdAplicativo;
    private static Integer seguridadTiempoExpiraSegundos;
    private static Integer seguridadTiempoRefreshSegundos;
    private static int timeoutBdTransactionSegundos;
    private static int timeoutClientApiConectionSegundos;
    private static int timeoutClientApiReadSegundos;
    private static String captchaUrl;
    private static String captchaToken;

    public ProjectProperties(
            @Value("${configuracion.seguridad.secretToken:null}") String seguridadSecretToken,
            @Value("${configuracion.seguridad.idaplicativo:0}") Integer seguridadIdAplicativo,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.expira.segundos:300}") Integer seguridadTiempoExpiraSegundos,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.refresh.segundos:180}") Integer seguridadTiempoRefreshSegundos,
            @Value("${timeout.database.transaction.segundos:120}") int timeoutBdTransactionSegundos,
            @Value("${timeout.client.api.conection.segundos:60}") int timeoutClientApiConectionSegundos,
            @Value("${timeout.client.api.read.segundos:60}") int timeoutClientApiReadSegundos,
        	@Value("${captcha.url:null}") String captchaUrl,
        	@Value("${captcha.token:null}") String captchaToken) {

        ProjectProperties.seguridadSecretToken = seguridadSecretToken;
        ProjectProperties.seguridadIdAplicativo = seguridadIdAplicativo;
        ProjectProperties.seguridadTiempoExpiraSegundos = seguridadTiempoExpiraSegundos;
        ProjectProperties.seguridadTiempoRefreshSegundos = seguridadTiempoRefreshSegundos;
        ProjectProperties.timeoutBdTransactionSegundos = timeoutBdTransactionSegundos;
        ProjectProperties.timeoutClientApiConectionSegundos = timeoutClientApiConectionSegundos;
        ProjectProperties.timeoutClientApiReadSegundos = timeoutClientApiReadSegundos;
        ProjectProperties.captchaUrl = captchaUrl;
        ProjectProperties.captchaToken = captchaToken;
    }

	public static String getSeguridadSecretToken() {
		return seguridadSecretToken;
	}

	public static Integer getSeguridadIdAplicativo() {
		return seguridadIdAplicativo;
	}

	public static Integer getSeguridadTiempoExpiraSegundos() {
		return seguridadTiempoExpiraSegundos;
	}

	public static Integer getSeguridadTiempoRefreshSegundos() {
		return seguridadTiempoRefreshSegundos;
	}

	public static int getTimeoutBdTransactionSegundos() {
		return timeoutBdTransactionSegundos;
	}

	public static int getTimeoutClientApiConectionSegundos() {
		return timeoutClientApiConectionSegundos;
	}

	public static int getTimeoutClientApiReadSegundos() {
		return timeoutClientApiReadSegundos;
	}

	public static String getCaptchaUrl() {
		return captchaUrl;
	}

	public static String getCaptchaToken() {
		return captchaToken;
	}

}
