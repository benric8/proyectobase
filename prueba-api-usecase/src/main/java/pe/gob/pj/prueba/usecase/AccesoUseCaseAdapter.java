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
import pe.gob.pj.prueba.domain.exceptions.CredencialesSinCoincidenciaException;
import pe.gob.pj.prueba.domain.exceptions.OpcionesNoAsignadadException;
import pe.gob.pj.prueba.domain.exceptions.UsuarioSinPerfilAsignadoException;
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
  public Usuario iniciarSesion(String cuo, String usuario, String clave){

    var user = accesoPersistencePort.iniciarSesion(cuo, usuario);
    var password = EncryptUtils.cryptBase64u(clave, Cipher.ENCRYPT_MODE);

    if (Objects.isNull(user) || user.getClave().isBlank() || !user.getClave().equals(password)) {
      throw new CredencialesSinCoincidenciaException();
    }

    if (user.getPerfiles().isEmpty()) {
      throw new UsuarioSinPerfilAsignadoException();
    }

    user.setClave("******");

    return user;
  }

  @Override
  @Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW,
      readOnly = true, rollbackFor = {Exception.class, SQLException.class})
  public PerfilOpcions obtenerOpciones(String cuo, Integer idPerfil){
    var perfilOpciones = accesoPersistencePort.obtenerOpciones(cuo, idPerfil);
    if (perfilOpciones.getOpciones().isEmpty()) {
      throw new OpcionesNoAsignadadException();
    }
    return perfilOpciones;
  }

}
