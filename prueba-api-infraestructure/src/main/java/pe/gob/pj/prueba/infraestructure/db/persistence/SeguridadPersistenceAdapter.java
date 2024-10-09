package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;
import pe.gob.pj.prueba.domain.model.seguridad.query.AutenticacionUsuarioQuery;
import pe.gob.pj.prueba.domain.port.persistence.SeguridadPersistencePort;
import pe.gob.pj.prueba.domain.utils.EncryptUtils;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repository.MaeRolRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repository.MaeRolUsuarioRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repository.MaeUsuarioRepository;
import pe.gob.pj.prueba.infraestructure.enums.Estado;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SeguridadPersistenceAdapter implements SeguridadPersistencePort {

  final MaeRolUsuarioRepository maeRolUsuarioRepository;
  final MaeUsuarioRepository maeUsuarioRepository;
  final MaeRolRepository maeRolRepository;

  @Override
  public String autenticarUsuario(String cuo, AutenticacionUsuarioQuery query) throws Exception {
    var nAplicacion = ProjectProperties.getSeguridadIdAplicativo();
    return maeRolUsuarioRepository
        .autenticarUsuario(query.usuario(), EncryptUtils.encrypt(query.usuario(), query.clave()),
            query.codigoRol(), query.codigoCliente(), nAplicacion)
        .map(usuario -> usuario.getNusuario().toString()).orElse(null);
  }

  @Override
  public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception {
    return maeUsuarioRepository.findById(Integer.valueOf(id))
        .map(usuario -> Usuario.builder().id(usuario.getNusuario()).cUsuario(usuario.getCUsuario())
            .cClave(usuario.getCClave()).lActivo(usuario.getActivo()).build())
        .orElse(null);
  }

  @Override
  public List<Rol> recuperarRoles(String cuo, String id) throws Exception {
    return maeRolRepository
        .findByActivoAndMaeRolUsuariosMaeUsuarioNusuario(Estado.ACTIVO_NUMERICO.getNombre(),
            Integer.parseInt(id))
        .stream().map(rol -> new Rol(rol.getNRol(), rol.getCRol(), rol.getXRol(), rol.getActivo()))
        .toList();
  }

  @Override
  public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion)
      throws Exception {
    var rpta = new StringBuilder("");
    maeRolUsuarioRepository.obtenerAccesoMetodo(usuario, rol, operacion)
        .ifPresent(rolusuario -> rolusuario.getMaeRol().getMaeOperacions().stream()
            .filter(x -> x.getXEndpoint().equalsIgnoreCase(operacion))
            .forEach(x -> rpta.append(x.getXOperacion())));
    return rpta.toString();
  }
}
