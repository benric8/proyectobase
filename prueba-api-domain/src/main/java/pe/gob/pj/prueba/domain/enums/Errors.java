package pe.gob.pj.prueba.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Errors {

	OPERACION_EXITOSA("0000","La operación se realizo de manera exitosa."),
	DATOS_NO_ENCONTRADOS("0001","Datos no encontrados al %s."),
	
	ERROR_INESPERADO("E000"," Error al %s - Error inesperado."),
	ERROR_SESSION_EXPIERADO("E001","La sesión ha expirado."),
	ERROR_TOKEN_NO_VALIDO("E002","No se pudo validar el token."),
	ERROR_REFRESCAR_TOKEN_TIEMPO_EXPIRADO("E003","El tiempo límite para refrescar el token enviado a expirado."),
	ERROR_EJECUCION_SP("E004"," Error al %s - Problemas en la ejecución del SP: %s"),
	ERROR_CONEXION_SEGURIDAD_NO_ESTABLECIDA("E005"," - No se estableció conexión con la BD PJ Seguridad."),
	ERROR_CREDENCIALES_SIJ_NO_EXISTE("E006"," Error al %s - No se puede obtener datos de configuración de la BD: %s"),
	ERROR_CREDENCIALES_SIJ_NO_ACEPTADAS("E007"," Error al %s - Problemas con las credenciales de conexión a la BD: %s"),
	ERROR_FTP_SUBIR_ARCHIVO_ERROR("E009"," Error al %s - No se pudo subir documento al ftp indicado."),
	ERROR_FTP_DESCARGAR_ARCHIVO("E010"," Error al %s - No se pudo descargar documento del ftp indicado."),
	ERROR_ALFRESCO_SUBIR_ARCHIVO("E011"," Error al %s - No se pudo subir documento al alfresco indicado."),
	ERROR_ALFRESCO_DESCARGAR_ARCHIVO("E012"," Error al %s - No se pudo descargar documento del alfresco indicado."),
	ERROR_CONSUMIR_SERVICIO_EXTERNO("E013"," Error al %s - Problemas con el consumo del endpoint: %"),
	ERROR_TOKEN_CAPTCHA("E014"," Error al %s - Se indica que se valide el captcha y el token captcha es nulo o no se pudo validar correctamente."),
	ERROR_AUDITORIA_GENERAL_REGISTRAR("E015", " Error al %s - No se pudo guardar trazabilidad en la auditoría general."),
	
	NEGOCIO_CREDENCIALES_INCORRECTAS("N001"," Error al %s - Las credenciales son incorrectas o el usuario esta inactivo."),
    NEGOCIO_PERFIL_NO_ASIGNADO("N002"," Error al %s - El usuario no tiene pefil asignado."),
	NEGOCIO_OPCIONES_NOASIGNADAS("N003"," Error al %s - El perfil de usuario no tiene opciones de menú asignadas."),
	NEGOCIO_PERSONA_YA_REGISTRADA("N004"," Error al %s - La persona ingresada ya esta registrada.");
	
	String codigo;
	String nombre;
	
	Errors(String codigo, String nombre){
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
}
