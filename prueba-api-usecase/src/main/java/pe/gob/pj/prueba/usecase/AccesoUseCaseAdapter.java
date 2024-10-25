package pe.gob.pj.prueba.usecase;


import java.sql.SQLException;
import java.util.Objects;
import javax.crypto.Cipher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;
import pe.gob.pj.prueba.domain.port.persistence.AccesoPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.AccesoUseCasePort;
import pe.gob.pj.prueba.domain.utils.EncryptUtils;

@Service("accesoUseCasePort")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccesoUseCaseAdapter implements AccesoUseCasePort {

  final AccesoPersistencePort accesoPersistencePort;

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public Usuario iniciarSesion(String cuo, String usuario, String clave) throws Exception {

    var user = accesoPersistencePort.iniciarSesion(cuo, usuario);
    var password = EncryptUtils.cryptBase64u(clave, Cipher.ENCRYPT_MODE);

    if (Objects.isNull(user) || user.getClave().isBlank() || !user.getClave().equals(password)) {
      throw new ErrorException(Errors.NEGOCIO_CREDENCIALES_INCORRECTAS.getCodigo(), String.format(
          Errors.NEGOCIO_CREDENCIALES_INCORRECTAS.getNombre(), Proceso.INICIAR_SESION.getNombre()));
    }

    if (user.getPerfiles().isEmpty()) {
      throw new ErrorException(Errors.NEGOCIO_PERFIL_NO_ASIGNADO.getCodigo(), String.format(
          Errors.NEGOCIO_PERFIL_NO_ASIGNADO.getNombre(), Proceso.INICIAR_SESION.getNombre()));
    }

    user.setClave("******");

    return user;
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public PerfilOpcions obtenerOpciones(String cuo, Integer idPerfil) throws Exception {
    var perfilOpciones = accesoPersistencePort.obtenerOpciones(cuo, idPerfil);
    if (perfilOpciones.getOpciones().isEmpty()) {
      throw new ErrorException(Errors.DATOS_NO_ENCONTRADOS.getCodigo(), String.format(
          Errors.NEGOCIO_OPCIONES_NOASIGNADAS.getNombre(), Proceso.OBTENER_OPCIONES.getNombre()));
    }
    return perfilOpciones;
  }

}
