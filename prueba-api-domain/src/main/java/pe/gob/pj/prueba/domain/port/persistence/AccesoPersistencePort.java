package pe.gob.pj.prueba.domain.port.persistence;

import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;

public interface AccesoPersistencePort {
	/**
	 * 
	 * Método que permite obtener datos de la entidad MovUsuario y encapsularlos en el modelo
	 * Usuario si hay coincidencia con el usuario enviado.
	 * 
	 * @param cuo Código unico de operación
	 * @param usuario Usuario con el que se quiere encontrar coincidencia
	 * @return Modelo Usuario o null
	 * @throws Exception
	 */
	public Usuario iniciarSesion(String cuo, String usuario);
	
	/**
	 * 
	 * Método que permite obtener una lista de la entidad MaeOpcion asignadas a la entidad MaePerfil y 
	 * encapsularlo en el modelo PerfilOpcions
	 * 
	 * @param cuo Código unico de operación
	 * @param idPerfil Identificador de perfil del cual se quiere obtener las opciones
	 * @return Modelo PerfilOpcions o null
	 * @throws Exception
	 */
	public PerfilOpcions obtenerOpciones(String cuo, Integer idPerfil);
}
