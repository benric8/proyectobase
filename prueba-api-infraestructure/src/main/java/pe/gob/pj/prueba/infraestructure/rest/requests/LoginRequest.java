package pe.gob.pj.prueba.infraestructure.rest.requests;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.common.utils.ProjectConstants;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest implements Serializable{

	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "formatoRespuesta")
	String formatoRespuesta;
	
	@NotBlank(message = "El parámetro usuario no puede tener un valor vacío.")
	@NotNull(message = "El parámetro usuario no puede tener un valor nulo.")
	@JsonProperty(value = "usuario")
	String usuario;

	@NotBlank(message = "El parámetro clave no puede tener un valor vacío.")
	@NotNull(message = "El parámetro clave no puede tener un valor nulo.")
	@JsonProperty(value = "clave")
	String clave;

	@Pattern(regexp = ProjectConstants.Pattern.S_N, message = "El parámetro aplicaCaptcha tiene un formato no válido [S|N].")
	@NotNull(message = "El parámetro aplicaCaptcha no puede ser nulo.")
	@JsonProperty("aplicaCaptcha")
	String aplicaCaptcha;

	@JsonProperty("tokenCaptcha")
	String tokenCaptcha;
	
}
