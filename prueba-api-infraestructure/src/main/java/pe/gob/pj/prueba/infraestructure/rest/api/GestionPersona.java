package pe.gob.pj.prueba.infraestructure.rest.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.rest.request.PersonaRequest;
import pe.gob.pj.prueba.infraestructure.rest.response.ConsultaPersonaResponse;
import pe.gob.pj.prueba.infraestructure.rest.response.PersonaResponse;

@RestController
@RequestMapping(value = "personas",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Tag(name = "GestionPersona", description = "API para administrar registros de personas")
public interface GestionPersona extends Base {

  /***
   * 
   * GET /personas : Consultar datos de persona
   * 
   * @param cuo
   * @param ips
   * @param usuauth
   * @param uri
   * @param params
   * @param herramienta
   * @param ip
   * @param formatoRespuesta
   * @param numeroDocumento
   * @return
   */
  @GetMapping
  @Operation(summary = "Consultar Persona", operationId = "consultarPersonas",
      description = "Permite consultar personas por filtro")
  @ApiResponse(responseCode = "200", description = "Consulta realizada",
      content = @Content(schema = @Schema(implementation = ConsultaPersonaResponse.class)))
  @ApiResponse(responseCode = "400", description = "Solicitud inválida")
  @ApiResponse(responseCode = "401", description = "El cliente no se autentico de manera correcta")
  @ApiResponse(responseCode = "403",
      description = "El cliente no esta autorizado para esta operación")
  public ResponseEntity<ConsultaPersonaResponse> consultarPersonas(
      @RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
      @RequestAttribute(name = ProjectConstants.AUD_IPS) String ips,
      @RequestAttribute(name = ProjectConstants.AUD_USUARIO) String usuauth,
      @RequestAttribute(name = ProjectConstants.AUD_URI) String uri,
      @RequestAttribute(name = ProjectConstants.AUD_PARAMS) String params,
      @RequestAttribute(name = ProjectConstants.AUD_HERRAMIENTA) String herramienta,
      @RequestAttribute(name = ProjectConstants.AUD_IP) String ip,
      @RequestParam(defaultValue = "json", required = false) String formatoRespuesta,
      @Size(min = 8, max = 8,
          message = "El parámetro numero_documento tiene un tamaño no valido [min1,max=1].") @Pattern(
              regexp = ProjectConstants.Pattern.NUMBER,
              message = "El parámetro numero_documento solo permite valores numéricos.") @RequestParam(
                  name = "numero_documento", required = false) String numeroDocumento);

  /***
   * 
   * POST /personas/crear : Crear persona en base a los datos enviados
   * 
   * @param cuo
   * @param ips
   * @param usuauth
   * @param uri
   * @param params
   * @param herramienta
   * @param ip
   * @param persona
   * @return
   */
  @PostMapping(value = "crear")
  @Operation(summary = "Registrar Persona", operationId = "registrarPersona",
      description = "Permite registrar persona")
  @ApiResponse(responseCode = "200", description = "Registro exitoso",
      content = @Content(schema = @Schema(implementation = PersonaResponse.class)))
  @ApiResponse(responseCode = "400", description = "Solicitud inválida")
  @ApiResponse(responseCode = "401", description = "El cliente no se autentico de manera correcta")
  @ApiResponse(responseCode = "403",
      description = "El cliente no esta autorizado para esta operación")
  public ResponseEntity<PersonaResponse> registrarPersona(
      @RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
      @RequestAttribute(name = ProjectConstants.AUD_IPS) String ips,
      @RequestAttribute(name = ProjectConstants.AUD_USUARIO) String usuauth,
      @RequestAttribute(name = ProjectConstants.AUD_URI) String uri,
      @RequestAttribute(name = ProjectConstants.AUD_PARAMS) String params,
      @RequestAttribute(name = ProjectConstants.AUD_HERRAMIENTA) String herramienta,
      @RequestAttribute(name = ProjectConstants.AUD_IP) String ip,
      @Validated @RequestBody PersonaRequest persona);

  /***
   * 
   * PUT /personas/actualizar/{id} : Actualizar persona en base al id
   * 
   * @param cuo
   * @param ips
   * @param usuauth
   * @param uri
   * @param params
   * @param herramienta
   * @param ip
   * @param id
   * @param persona
   * @return
   */
  @PutMapping(value = "actualizar/{id}")
  @Operation(summary = "Actualizar Persona", operationId = "actualizarPersona",
      description = "Permite actualizar datos de una persona")
  @ApiResponse(responseCode = "200", description = "Actualización correcta",
      content = @Content(schema = @Schema(implementation = PersonaResponse.class)))
  @ApiResponse(responseCode = "400", description = "Solicitud inválida")
  @ApiResponse(responseCode = "401", description = "El cliente no se autentico de manera correcta")
  @ApiResponse(responseCode = "403",
      description = "El cliente no esta autorizado para esta operación")
  public ResponseEntity<PersonaResponse> actualizarPersona(
      @RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
      @RequestAttribute(name = ProjectConstants.AUD_IPS) String ips,
      @RequestAttribute(name = ProjectConstants.AUD_USUARIO) String usuauth,
      @RequestAttribute(name = ProjectConstants.AUD_URI) String uri,
      @RequestAttribute(name = ProjectConstants.AUD_PARAMS) String params,
      @RequestAttribute(name = ProjectConstants.AUD_HERRAMIENTA) String herramienta,
      @RequestAttribute(name = ProjectConstants.AUD_IP) String ip, @PathVariable Integer id,
      @Validated @RequestBody PersonaRequest persona);

}
