package pe.gob.pj.prueba.infraestructure.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ObtenerOpcionesRequest implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "formatoRespuesta")
	String formatoRespuesta;
	
	@NotNull(message = "El usuario no puede ser nulo.")
	@NotBlank(message = "El usuario no puede ser vacío.")
	@Length(min = 8, message = "El usuario tiene una longitud no válida [min=8].")
	@JsonProperty("usuario")
	private String usuario;

	@NotNull(message = "Se requiere enviar identificador de perfil.")
	@JsonProperty("idPerfil")
	private int idPerfil;

}
