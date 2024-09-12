package pe.gob.pj.prueba.infraestructure.rest;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.rest.request.LoginRequest;
import pe.gob.pj.prueba.infraestructure.rest.request.ObtenerOpcionesRequest;
import pe.gob.pj.prueba.infraestructure.rest.response.GlobalResponse;

@RestController
@RequestMapping(value = "authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
public interface Acceso extends Base{

	/***
	 * 
	 * POST /authenticate/login : Iniciar sesión
	 * 
	 * @param cuo
	 * @param ip
	 * @param jwt
	 * @param login
	 * @return
	 */
	@PostMapping(value = "login")
	public ResponseEntity<GlobalResponse> iniciarSesion(
			@RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
			@RequestAttribute(name = ProjectConstants.AUD_IP) String ip,
			@RequestAttribute(name = ProjectConstants.AUD_JWT) String jwt, 
			@Valid @RequestBody LoginRequest login);
	
	/***
	 * 
	 * POST /authenticate/opciones : Obtener las opciones del perfil enviado
	 * 
	 * @param cuo
	 * @param ip
	 * @param jwt
	 * @param perfil
	 * @return
	 */
	@PostMapping(value = "opciones")
	public ResponseEntity<GlobalResponse> obtenerOpciones(
			@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo, 
			@RequestAttribute(name=ProjectConstants.AUD_IP) String ip, 
			@RequestAttribute(name=ProjectConstants.AUD_JWT) String jwt,
			@Valid @RequestBody ObtenerOpcionesRequest perfil);

}
