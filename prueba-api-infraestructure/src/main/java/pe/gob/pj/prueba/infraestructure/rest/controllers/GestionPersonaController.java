package pe.gob.pj.prueba.infraestructure.rest.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.auditoriageneral.PeticionServicios;
import pe.gob.pj.prueba.domain.model.servicio.query.ConsultarPersonaQuery;
import pe.gob.pj.prueba.domain.port.usecase.AuditoriaGeneralUseCasePort;
import pe.gob.pj.prueba.domain.port.usecase.GestionPersonaUseCasePort;
import pe.gob.pj.prueba.infraestructure.client.servicioconsumir.services.TestClient;
import pe.gob.pj.prueba.infraestructure.common.enums.FormatoRespuesta;
import pe.gob.pj.prueba.infraestructure.mappers.AuditoriaGeneralMapper;
import pe.gob.pj.prueba.infraestructure.mappers.PersonaMapper;
import pe.gob.pj.prueba.infraestructure.rest.requests.PersonaRequest;
import pe.gob.pj.prueba.infraestructure.rest.responses.ConsultaPersonaResponse;
import pe.gob.pj.prueba.infraestructure.rest.responses.PersonaResponse;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class GestionPersonaController implements GestionPersona {

  @SuppressWarnings("unused")
  final TestClient clientSunarp;
  final GestionPersonaUseCasePort gestionPersonaUseCasePort;
  final AuditoriaGeneralUseCasePort auditoriaGeneralUseCasePort;
  final PersonaMapper personaMapper;
  final AuditoriaGeneralMapper auditoriaGeneralMapper;

  @Override
  public ResponseEntity<ConsultaPersonaResponse> consultarPersonas(PeticionServicios peticion,
      String formatoRespuesta, String numeroDocumento) {
    var res = new ConsultaPersonaResponse();
    res.setCodigoOperacion(peticion.getCuo());

    res.setData(gestionPersonaUseCasePort.buscarPersona(peticion.getCuo(),
        ConsultarPersonaQuery.builder().documentoIdentidad(numeroDocumento).build()));

    var headers = new HttpHeaders();
    headers.setContentType(
        MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(formatoRespuesta)
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PersonaResponse> registrarPersona(PeticionServicios peticion,
      PersonaRequest request) {
    var res = new PersonaResponse();
    res.setCodigoOperacion(peticion.getCuo());

    var inicio = System.currentTimeMillis();
    var personaDto = personaMapper.toPersona(request);
    gestionPersonaUseCasePort.registrarPersona(peticion.getCuo(), personaDto);
    res.setData(personaDto);
    var fin = System.currentTimeMillis();

    try {
      var auditoriaAplicativos =
          auditoriaGeneralMapper.toAuditoriaAplicativos(request.getAuditoria(), peticion.getCuo(),
              peticion.getIps(), peticion.getUsuario(), peticion.getUri(), peticion.getParams(),
              peticion.getHerramienta(), res.getCodigo(), res.getDescripcion(), fin - inicio);
      var objectMapper = new ObjectMapper();
      var jsonString = objectMapper.writeValueAsString(request);
      auditoriaAplicativos.setPeticionBody(jsonString);
      auditoriaGeneralUseCasePort.crear(peticion.getCuo(), auditoriaAplicativos);
    } catch (JsonProcessingException e) {
      log.error("{} JsonProcessingException {} ", peticion.getCuo(), e);
    }

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(
        FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta())
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PersonaResponse> actualizarPersona(PeticionServicios peticion, Integer id,
      PersonaRequest request) {
    var res = new PersonaResponse();
    res.setCodigoOperacion(peticion.getCuo());

    var personaDto = personaMapper.toPersona(request);
    personaDto.setId(id);
    gestionPersonaUseCasePort.actualizarPersona(peticion.getCuo(), personaDto);
    res.setData(personaDto);

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(
        FormatoRespuesta.XML.getNombre().equalsIgnoreCase(request.getFormatoRespuesta())
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return new ResponseEntity<>(res, headers, HttpStatus.OK);
  }

}
