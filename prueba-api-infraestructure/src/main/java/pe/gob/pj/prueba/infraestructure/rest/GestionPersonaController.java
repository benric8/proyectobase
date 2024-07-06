package pe.gob.pj.prueba.infraestructure.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.port.usecase.AuditoriaGeneralUseCasePort;
import pe.gob.pj.prueba.domain.port.usecase.GestionPersonaUseCasePort;
import pe.gob.pj.prueba.infraestructure.client.servicioconsumir.services.TestClient;
import pe.gob.pj.prueba.infraestructure.enums.FormatoRespuesta;
import pe.gob.pj.prueba.infraestructure.mapper.AuditoriaGeneralMapper;
import pe.gob.pj.prueba.infraestructure.mapper.PersonaMapper;
import pe.gob.pj.prueba.infraestructure.rest.request.PersonaRequest;
import pe.gob.pj.prueba.infraestructure.rest.response.GlobalResponse;

@RestController
public class GestionPersonaController implements GestionPersona, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("sunarpClient")
	private TestClient clientSunarp;

	final GestionPersonaUseCasePort gestionPersonaUseCasePort;
	final AuditoriaGeneralUseCasePort auditoriaGeneralUseCasePort;
	final PersonaMapper personaMapper;
	final AuditoriaGeneralMapper auditoriaGeneralMapper;
	
	public GestionPersonaController(@Qualifier("gestionPersonaUseCasePort") GestionPersonaUseCasePort gestionPersonaUseCasePort,
			AuditoriaGeneralUseCasePort auditoriaGeneralUseCasePort,
			PersonaMapper personaMapper,AuditoriaGeneralMapper auditoriaGeneralMapper){
		this.gestionPersonaUseCasePort = gestionPersonaUseCasePort;
		this.auditoriaGeneralUseCasePort = auditoriaGeneralUseCasePort;
		this.auditoriaGeneralMapper = auditoriaGeneralMapper;
		this.personaMapper = personaMapper;
	}

	@Override
	public ResponseEntity<GlobalResponse> consultarPersonas(String cuo, String ips, String usuauth, String uri,
			String params, String herramienta, String ip,String formatoRespuesta, String numeroDocumento) {
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo);
		
		try {
			res.setCodigo(Errors.OPERACION_EXITOSA.getCodigo());
			res.setDescripcion(Errors.OPERACION_EXITOSA.getNombre());
			Map<String, Object> filters = new HashMap<String, Object>();
			filters.put(Persona.P_NUMERO_DOCUMENTO, numeroDocumento);
			res.setData(gestionPersonaUseCasePort.buscarPersona(cuo, filters));
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo,
					new ErrorException(
							Errors.ERROR_INESPERADO.getCodigo(), Errors.ERROR_AL.getNombre()
									+ Proceso.PERSONA_CONSULTAR.getNombre() + Errors.ERROR_INESPERADO.getNombre(),
							e.getMessage(), e.getCause()),
					res);
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_XML_VALUE : MediaType.APPLICATION_JSON_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GlobalResponse> registrarPersona(String cuo, String ips, String usuauth, String uri,
			String params, String herramienta, String ip, PersonaRequest request) {
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo);
		try {
			long inicio = System.currentTimeMillis();
			res.setCodigo(Errors.OPERACION_EXITOSA.getCodigo());
			res.setDescripcion(Errors.OPERACION_EXITOSA.getNombre());
			Persona personaDto = personaMapper.toPersona(request);
			gestionPersonaUseCasePort.registrarPersona(cuo, personaDto);
			res.setData(personaDto);
			long fin = System.currentTimeMillis();
			AuditoriaAplicativos auditoriaAplicativos = auditoriaGeneralMapper.toAuditoriaAplicativos(
					request.getAuditoria(), cuo, ips, usuauth, uri, params, herramienta, res.getCodigo(),
					res.getDescripcion(), fin - inicio);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(request);
			auditoriaAplicativos.setPeticionBody(jsonString);
			auditoriaGeneralUseCasePort.crear(cuo, auditoriaAplicativos);
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo,
					new ErrorException(
							Errors.ERROR_INESPERADO.getCodigo(), Errors.ERROR_AL.getNombre()
									+ Proceso.PERSONA_REGISTRAR.getNombre() + Errors.ERROR_INESPERADO.getNombre(),
							e.getMessage(), e.getCause()),
					res);
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta()) ? MediaType.APPLICATION_XML_VALUE : MediaType.APPLICATION_JSON_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GlobalResponse> actualizarPersona(String cuo, String ips, String usuauth, String uri,
			String params, String herramienta, String ip, Integer id, PersonaRequest request) {
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo);
		try {
			res.setCodigo(Errors.OPERACION_EXITOSA.getCodigo());
			res.setDescripcion(Errors.OPERACION_EXITOSA.getNombre());
			Persona personaDto = personaMapper.toPersona(request);
			personaDto.setId(id);
			gestionPersonaUseCasePort.actualizarPersona(cuo, personaDto);
			res.setData(personaDto);
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo,
					new ErrorException(
							Errors.ERROR_INESPERADO.getCodigo(), Errors.ERROR_AL.getNombre()
									+ Proceso.PERSONA_ACTUALIZAR.getNombre() + Errors.ERROR_INESPERADO.getNombre(),
							e.getMessage(), e.getCause()),
					res);
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta()) ? MediaType.APPLICATION_XML_VALUE : MediaType.APPLICATION_JSON_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}

}
