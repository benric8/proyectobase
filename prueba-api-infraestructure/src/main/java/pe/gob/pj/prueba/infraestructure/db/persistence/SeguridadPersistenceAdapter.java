package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.common.utils.EncryptUtils;
import pe.gob.pj.prueba.domain.common.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;
import pe.gob.pj.prueba.domain.model.seguridad.query.AutenticacionUsuarioQuery;
import pe.gob.pj.prueba.domain.port.persistence.SeguridadPersistencePort;
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entities.MaeOperacionEntity;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repositories.MaeRolRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repositories.MaeRolUsuarioRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.repositories.MaeUsuarioRepository;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SeguridadPersistenceAdapter implements SeguridadPersistencePort {

  final MaeRolUsuarioRepository maeRolUsuarioRepository;
  final MaeUsuarioRepository maeUsuarioRepository;
  final MaeRolRepository maeRolRepository;

  @Override
  public String autenticarUsuario(String cuo, AutenticacionUsuarioQuery query) {
    var nAplicacion = ProjectProperties.getSeguridadIdAplicativo();
    return maeRolUsuarioRepository
        .autenticarUsuario(query.usuario(), EncryptUtils.encrypt(query.usuario(), query.clave()),
            query.codigoRol(), query.codigoCliente(), nAplicacion)
        .map(usuario -> usuario.getNusuario().toString()).orElse(null);
  }

  @Override
  public Usuario recuperaInfoUsuario(String cuo, String id) {
    return maeUsuarioRepository.findById(Integer.valueOf(id))
        .map(usuario -> Usuario.builder().id(usuario.getNusuario()).cUsuario(usuario.getCUsuario())
            .cClave(usuario.getCClave()).lActivo(usuario.getActivo()).build())
        .orElse(null);
  }

  @Override
  public List<Rol> recuperarRoles(String cuo, String id) {
    return maeRolRepository
        .findByActivoAndMaeRolUsuariosMaeUsuarioNusuario(Estado.ACTIVO_NUMERICO.getNombre(),
            Integer.parseInt(id))
        .stream().map(rol -> new Rol(rol.getNRol(), rol.getCRol(), rol.getXRol(), rol.getActivo()))
        .toList();
  }

  @Override
  public Optional<String> validarAccesoMetodo(String cuo, String usuario, String rol, String operacion) {
    return maeRolUsuarioRepository.obtenerAccesoMetodos(usuario, rol).stream()
        .filter(op->Pattern.compile(op.getXEndpoint()).matcher(operacion).matches())
        .map(MaeOperacionEntity::getXOperacion).findFirst();
  }
}
