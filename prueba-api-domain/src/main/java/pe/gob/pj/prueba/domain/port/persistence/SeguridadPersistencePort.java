package pe.gob.pj.prueba.domain.port.persistence;

import java.util.List;

import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;

public interface SeguridadPersistencePort {
	public String autenticarUsuario(String cuo, String codigoCliente, String codigoRol, String usuario, String clave) throws Exception;
	public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception;
	public List<Rol> recuperarRoles(String cuo, String id) throws Exception;
	public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion) throws Exception;
}
