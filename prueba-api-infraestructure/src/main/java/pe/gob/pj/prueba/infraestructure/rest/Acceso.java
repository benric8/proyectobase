package pe.gob.pj.prueba.infraestructure.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.rest.request.LoginRequest;
import pe.gob.pj.prueba.infraestructure.rest.request.ObtenerOpcionesRequest;
import pe.gob.pj.prueba.infraestructure.rest.response.PerfilOpcionesResponse;
import pe.gob.pj.prueba.infraestructure.rest.response.UsuarioResponse;

@RestController
@RequestMapping(value = "authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Acceso", description = "API para autenticación y gestión de accesos")
public interface Acceso extends Base {

  @PostMapping(value = "login")
  @Operation(summary = "Iniciar sesión", operationId = "iniciarSesion",
      description = "Permite a un usuario iniciar sesión en el sistema")
  @ApiResponse(responseCode = "200", description = "Sesión iniciada correctamente",
      content = @Content(schema = @Schema(implementation = UsuarioResponse.class)))
  @ApiResponse(responseCode = "400", description = "Solicitud inválida")
  @ApiResponse(responseCode = "401", description = "El cliente no se autentico de manera correcta")
  @ApiResponse(responseCode = "403",
      description = "El cliente no esta autorizado para esta operación")
  public ResponseEntity<UsuarioResponse> iniciarSesion(
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_IP) String ip,
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_JWT) String jwt,
      @Valid @RequestBody LoginRequest login);

  @PostMapping(value = "opciones")
  @Operation(summary = "Obtener opciones del perfil", operationId = "obtenerOpciones",
      description = "Obtiene las opciones disponibles para un perfil específico")
  @ApiResponse(responseCode = "200", description = "Opciones obtenidas correctamente",
      content = @Content(schema = @Schema(implementation = PerfilOpcionesResponse.class)))
  @ApiResponse(responseCode = "400", description = "Solicitud inválida")
  @ApiResponse(responseCode = "401", description = "El cliente no se autentico de manera correcta")
  @ApiResponse(responseCode = "403",
      description = "El cliente no esta autorizado para esta operación")
  public ResponseEntity<PerfilOpcionesResponse> obtenerOpciones(
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_IP) String ip,
      @Parameter(hidden = true) @RequestAttribute(name = ProjectConstants.AUD_JWT) String jwt,
      @Valid @RequestBody ObtenerOpcionesRequest perfil);
}
