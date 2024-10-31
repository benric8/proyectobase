package pe.gob.pj.prueba.domain.port.usecase;

import java.util.List;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.model.servicio.query.ConsultarPersonaQuery;

public interface GestionPersonaUseCasePort {

	/**
	 * 
	 * Método que permite enviar filtros para obtener una lista del modelo persona
	 * 
	 * @param cuo Código unico de operación
	 * @param filters Lista de clave valor, donde la clave son los parámetros declarados en el modelo Persona
	 * @return Lista del modelo Persona que coinciden con los filtros enviados
	 * @throws Exception
	 */
	public List<Persona> buscarPersona(String cuo, ConsultarPersonaQuery query);

	/**
	 * 
	 * Método que permite enviar los datos encapsulados en el modelo Persona para ser guardados
	 * aplicando reglas propias del negocio
	 * 
	 * @param cuo Código unico de operación
	 * @param persona Modelo que contiene los atributos de una persona que se quieren guardar
	 * @throws Exception
	 */
	public void registrarPersona(String cuo, Persona persona);

	/**
	 * 
	 * Método que permite enviar los datos encapsulados en el modelo Persona para ser actualizados
	 * aplicando reglas propias del negocio
	 * 
	 * @param cuo Código unico de operación
	 * @param persona Modelo que contiene los atributos de una persona que se quieren guardar incluyendo el identificador
	 * @throws Exception
	 */
	public void actualizarPersona(String cuo, Persona persona);

}
