package pe.gob.pj.prueba.infraestructure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoError {

  OPERACION_EXITOSA("0000","La operación se realizo de manera exitosa."),
  DATOS_NO_ENCONTRADOS("0001","Datos no encontrados."),
  
  ERROR_INESPERADO("E000","Ocurrió un error inesperado, comuniquese con el área de soporte."),
  ERROR_TOKEN_ERRADO("E001","Ocurrió un error con el token de sesión."),
  ERROR_TOKEN_CAPTCHA("E014","Ocurrió un error, en la validación del captcha."),
  
  CREDENCIALES_INCORRECTAS("N001","Ocurrió un error, las credenciales son incorrectas o el usuario esta inactivo."),
  PERFIL_NO_ASIGNADO("N002","Ocurrió un error, el usuario no tiene pefil asignado."),
  OPCIONES_NOASIGNADAS("N003","Ocurrió un error, el perfil de usuario no tiene opciones de menú asignadas."),
  PERSONA_YA_REGISTRADA("N004","Ocurrió un error, la persona ingresada ya esta registrada.");
  
  private final String codigo;
  private final String descripcion;

}
