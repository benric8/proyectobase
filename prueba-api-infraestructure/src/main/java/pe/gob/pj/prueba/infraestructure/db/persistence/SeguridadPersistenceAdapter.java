package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.domain.model.seguridad.Usuario;
import pe.gob.pj.prueba.domain.port.persistence.SeguridadPersistencePort;
import pe.gob.pj.prueba.domain.utils.EncryptUtils;
import pe.gob.pj.prueba.domain.utils.ProjectProperties;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeRolEntity;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeRolUsuarioEntity;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeUsuarioEntity;

@Slf4j
@Component("seguridadPersistencePort")
public class SeguridadPersistenceAdapter implements SeguridadPersistencePort {

  @PersistenceContext(unitName = "seguridad")
  private EntityManager entityManager;

  @Override
  public String autenticarUsuario(String cuo, String codigoCliente, String codigoRol,
      String usuario, String clave) throws Exception {
    var user = new Usuario();
    var nAplicacion = ProjectProperties.getSeguridadIdAplicativo();
    Object[] params = {usuario, codigoRol, nAplicacion, codigoCliente};
    try {
      TypedQuery<MaeUsuarioEntity> query = this.entityManager
          .createNamedQuery(MaeRolUsuarioEntity.AUTENTICAR_USUARIO, MaeUsuarioEntity.class);
      query.setParameter(MaeRolUsuarioEntity.P_COD_USUARIO, usuario);
      query.setParameter(MaeRolUsuarioEntity.P_COD_ROL, codigoRol);
      query.setParameter(MaeRolUsuarioEntity.P_COD_CLIENTE, codigoCliente);
      query.setParameter(MaeRolUsuarioEntity.P_N_APLICATIVO, nAplicacion);
      var usr = query.getSingleResult();
      var claveFinal = EncryptUtils.encrypt(usuario, clave);
      if (ProjectUtils.isNull(usr.getCClave()).trim().equals(claveFinal)) {
        user.setId(usr.getNUsuario());
        user.setCClave(ProjectUtils.isNull(usr.getCClave()));
      }
    } catch (NoResultException not) {
      log.error("{} No se encontró usuario registrado en BD para los parámetros [{}]", cuo,
          params.toString());
    } catch (Exception e) {
      log.error("{} Ocurrió un eror : [{}]", cuo, e.getMessage());
    }
    return user.getId() == null ? null : user.getId().toString();
  }

  @Override
  public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception {
    var user = new Usuario();
    Object[] params = {Integer.parseInt(id)};
    try {
      TypedQuery<MaeUsuarioEntity> query =
          this.entityManager.createNamedQuery(MaeUsuarioEntity.FIND_BY_ID, MaeUsuarioEntity.class);
      query.setParameter(MaeUsuarioEntity.P_N_USUARIO, Integer.parseInt(id));
      var u = query.getSingleResult();
      user.setCClave(u.getCClave());
      user.setCUsuario(u.getCUsuario());
      user.setId(u.getNUsuario());
      user.setLActivo(u.getActivo());
    } catch (NoResultException not) {
      log.info(cuo.concat("No se encontro usuario registrado en BD").concat(params.toString()));
      user = null;
    } catch (Exception e) {
      log.error(cuo.concat(e.getMessage()));
      user = null;
    }
    return user;
  }

  @Override
  public List<Rol> recuperarRoles(String cuo, String id) throws Exception {
    var lista = new ArrayList<Rol>();
    Object[] params = {Integer.parseInt(id)};
    try {
      TypedQuery<MaeRolEntity> query = this.entityManager
          .createNamedQuery(MaeRolEntity.FIND_ROLES_BY_ID_USUARIO, MaeRolEntity.class);
      query.setParameter(MaeUsuarioEntity.P_N_USUARIO, Integer.parseInt(id));
      query.getResultStream().forEach(maeRol -> {
        lista
            .add(new Rol(maeRol.getNRol(), maeRol.getCRol(), maeRol.getXRol(), maeRol.getActivo()));
      });
    } catch (NoResultException not) {
      log.info(cuo.concat("No se encontro roles registrado en BD").concat(params.toString()));
    } catch (Exception e) {
      log.error(cuo.concat(e.getMessage()));
    }
    return lista;
  }

  @Override
  public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion)
      throws Exception {
    var rpta = new StringBuilder("");
    Object[] params = {usuario, rol, operacion};
    try {
      TypedQuery<MaeRolUsuarioEntity> query = this.entityManager
          .createNamedQuery(MaeRolUsuarioEntity.VALIDAR_ACCESO_METODO, MaeRolUsuarioEntity.class);
      query.setParameter(MaeRolUsuarioEntity.P_COD_USUARIO, usuario);
      query.setParameter(MaeRolUsuarioEntity.P_COD_ROL, rol);
      query.setParameter(MaeRolUsuarioEntity.P_OPERACION, operacion);
      var rolusuario = query.getResultStream().findFirst().orElse(null);
      if (rolusuario != null) {
        rolusuario.getMaeRol().getMaeOperacions().forEach(x -> {
          if (x.getXEndpoint().equalsIgnoreCase(operacion))
            rpta.append(x.getXOperacion());
        });
      }
    } catch (NoResultException not) {
      log.error("{} No se encontró permisos para los parámetros [{}]", cuo, params.toString());
    } catch (Exception e) {
      log.error(cuo.concat(e.getMessage()));
    }
    return rpta.toString();
  }
}
