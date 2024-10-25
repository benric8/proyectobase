package pe.gob.pj.prueba.infraestructure.rest.apis;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.servicio.query.ConsultarPersonaQuery;
import pe.gob.pj.prueba.domain.port.usecase.AuditoriaGeneralUseCasePort;
import pe.gob.pj.prueba.domain.port.usecase.GestionPersonaUseCasePort;
import pe.gob.pj.prueba.infraestructure.client.servicioconsumir.services.TestClient;
import pe.gob.pj.prueba.infraestructure.common.enums.FormatoRespuesta;
import pe.gob.pj.prueba.infraestructure.mapper.AuditoriaGeneralMapper;
import pe.gob.pj.prueba.infraestructure.mapper.PersonaMapper;
import pe.gob.pj.prueba.infraestructure.rest.requests.PersonaRequest;
import pe.gob.pj.prueba.infraestructure.rest.responses.ConsultaPersonaResponse;
import pe.gob.pj.prueba.infraestructure.rest.responses.PersonaResponse;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestionPersonaController implements GestionPersona {

  @SuppressWarnings("unused")
  final TestClient clientSunarp;
  final GestionPersonaUseCasePort gestionPersonaUseCasePort;
  final AuditoriaGeneralUseCasePort auditoriaGeneralUseCasePort;
  final PersonaMapper personaMapper;
  final AuditoriaGeneralMapper auditoriaGeneralMapper;

  @Override
  public ResponseEntity<ConsultaPersonaResponse> consultarPersonas(String cuo, String ips,
      String usuauth, String uri, String params, String herramienta, String ip,
      String formatoRespuesta, String numeroDocumento) {
    var res = new ConsultaPersonaResponse();
    res.setCodigoOperacion(cuo);

    try {
      res.setData(gestionPersonaUseCasePort.buscarPersona(cuo,
          ConsultarPersonaQuery.builder().documentoIdentidad(numeroDocumento).build()));
    } catch (ErrorException e) {
      handleException(cuo, e);
    } catch (Exception e) {
      res.errorInesperado(Proceso.PERSONA_CONSULTAR.getNombre());
      handleException(cuo,
          new ErrorException(res.getCodigo(), res.getDescripcion(), e.getMessage(), e.getCause()));
    }
    var headers = new HttpHeaders();
    headers.setContentType(
        MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(formatoRespuesta)
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PersonaResponse> registrarPersona(String cuo, String ips, String usuauth,
      String uri, String params, String herramienta, String ip, PersonaRequest request) {
    var res = new PersonaResponse();
    res.setCodigoOperacion(cuo);
    try {
      var inicio = System.currentTimeMillis();
      var personaDto = personaMapper.toPersona(request);
      gestionPersonaUseCasePort.registrarPersona(cuo, personaDto);
      res.setData(personaDto);
      var fin = System.currentTimeMillis();
      var auditoriaAplicativos =
          auditoriaGeneralMapper.toAuditoriaAplicativos(request.getAuditoria(), cuo, ips, usuauth,
              uri, params, herramienta, res.getCodigo(), res.getDescripcion(), fin - inicio);
      var objectMapper = new ObjectMapper();
      var jsonString = objectMapper.writeValueAsString(request);
      auditoriaAplicativos.setPeticionBody(jsonString);
      auditoriaGeneralUseCasePort.crear(cuo, auditoriaAplicativos);
    } catch (ErrorException e) {
      handleException(cuo, e);
    } catch (Exception e) {
      res.errorInesperado(Proceso.PERSONA_REGISTRAR.getNombre());
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
  public ResponseEntity<PersonaResponse> actualizarPersona(String cuo, String ips, String usuauth,
      String uri, String params, String herramienta, String ip, Integer id,
      PersonaRequest request) {
    var res = new PersonaResponse();
    res.setCodigoOperacion(cuo);
    try {
      var personaDto = personaMapper.toPersona(request);
      personaDto.setId(id);
      gestionPersonaUseCasePort.actualizarPersona(cuo, personaDto);
      res.setData(personaDto);
    } catch (ErrorException e) {
      res.setCodigo(e.getCodigo());
      res.setDescripcion(e.getDescripcion());
      handleException(cuo, e);
    } catch (Exception e) {
      res.errorInesperado(Proceso.PERSONA_ACTUALIZAR.getNombre());
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

}
