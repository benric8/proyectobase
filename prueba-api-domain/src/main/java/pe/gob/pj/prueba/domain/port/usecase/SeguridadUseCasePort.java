package pe.gob.pj.prueba.domain.port.usecase;

import java.util.List;
import java.util.Optional;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;
import pe.gob.pj.prueba.domain.model.seguridad.query.AutenticacionUsuarioQuery;


public interface SeguridadUseCasePort{
	public String autenticarUsuario(String cuo, AutenticacionUsuarioQuery query);
	public Usuario recuperaInfoUsuario(String cuo, String id);
	public List<Rol> recuperarRoles(String cuo, String id);
	public Optional<String> validarAccesoMetodo(String cuo, String usuario, String rol, String operacion);
}
