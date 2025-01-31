package pe.gob.pj.prueba.domain.port.usecase;

import pe.gob.pj.prueba.domain.model.auditoriageneral.PeticionServicios;
import pe.gob.pj.prueba.domain.model.negocio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.negocio.Usuario;
import pe.gob.pj.prueba.domain.model.negocio.query.IniciarSesionQuery;

public interface AccesoUseCasePort {

  /**
   * 
   * Método que permite enviar las credenciales para ser validadas y devolver un objeto con los
   * datos del usuario o lanza una ErrorException indicando que las credenciales no son válidas o el
   * usuario esta inactivo.
   * 
   * @param cuo Código unico de operación
   * @param usuario Usuario con el que se inicia sesión
   * @param clave Clave con la que se inicia sesión
   * @return Usuario
   * @throws Exception
   */
  public Usuario iniciarSesion(String cuo, IniciarSesionQuery iniciarSesionQuery,
      PeticionServicios peticion);

  /**
   * 
   * Método que permite enviar el identificador de perfil para ser validado si existe y devolver las
   * opciones asignadas a este perfil.
   * 
   * @param cuo Código unico de operación
   * @param idPerfil Identificador del perfil
   * @return Retorna las opciones asignadas al perfil
   * @throws Exception
   */
  public PerfilOpcions obtenerOpciones(String cuo, String usuario, Integer idPerfil,
      PeticionServicios peticion);
}
