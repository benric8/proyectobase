package pe.gob.pj.prueba.domain.port.persistence;

import java.util.List;
import pe.gob.pj.prueba.domain.model.negocio.Persona;
import pe.gob.pj.prueba.domain.model.negocio.query.ConsultarPersonaQuery;

public interface GestionPersonaPersistencePort {

	/**
	 * 
	 * Método que permite obtener una lista de entidad MovPersona y encapsularlo en una lista del modelo
	 * Persona, las cuales coinciden con los filtros enviados.
	 * 
	 * @param cuo Código unico de operación
	 * @param filters Lista de clave valor, donde la clave son los parámetros declarados en el modelo Persona
	 * @return Lista del modelo Persona que coinciden con los filtros enviados
	 * @throws Exception
	 */
	public List<Persona> buscarPersona(String cuo, ConsultarPersonaQuery filters);

	/**
	 * 
	 * Método que permite registrar entidad MovPersona con los datos encapsulados en el modelo Persona
	 * 
	 * @param cuo Código unico de operación
	 * @param persona Modelo que contiene los atributos de una persona que se quieren guardar
	 * @throws Exception
	 */
	public void registrarPersona(String cuo, Persona persona);

	/**
	 * 
	 * Método que permite actualizar entidad MovPersona con los datos encapsulados en el modelo Persona
	 * 
	 * @param cuo Código unico de operación
	 * @param persona Modelo que contiene los atributos de una persona que se quieren guardar incluyendo el identificador
	 * @throws Exception
	 */
	public void actualizarPersona(String cuo, Persona persona);

}
