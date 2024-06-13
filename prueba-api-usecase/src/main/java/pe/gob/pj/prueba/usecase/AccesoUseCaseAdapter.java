package pe.gob.pj.prueba.usecase;


import java.sql.SQLException;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.pj.prueba.domain.enums.Errors;
import pe.gob.pj.prueba.domain.enums.Proceso;
import pe.gob.pj.prueba.domain.exceptions.ErrorException;
import pe.gob.pj.prueba.domain.model.servicio.PerfilOpcions;
import pe.gob.pj.prueba.domain.model.servicio.Usuario;
import pe.gob.pj.prueba.domain.port.persistence.AccesoPersistencePort;
import pe.gob.pj.prueba.domain.port.usecase.AccesoUseCasePort;
import pe.gob.pj.prueba.domain.utils.EncryptUtils;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;

@Service("accesoUseCasePort")
public class AccesoUseCaseAdapter implements AccesoUseCasePort{
	
	final AccesoPersistencePort accesoPersistencePort;
	
	public AccesoUseCaseAdapter(@Qualifier("accesoPersistencePort") AccesoPersistencePort accesoPersistencePort) {
		this.accesoPersistencePort = accesoPersistencePort;
	}

	@Override
	@Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = {Exception.class, SQLException.class })
	public Usuario iniciarSesion(String cuo, String usuario, String clave) throws Exception {
		
		Usuario user = accesoPersistencePort.iniciarSesion(cuo, usuario);
		String password = EncryptUtils.cryptBase64u(clave, Cipher.ENCRYPT_MODE);
		
		if(user==null || ProjectUtils.isNullOrEmpty(user.getClave()) || !user.getClave().equals(password))
			throw new ErrorException(Errors.NEGOCIO_CREDENCIALES_INCORRECTAS.getCodigo(), 
					Errors.ERROR_AL.getNombre()+Proceso.INICIAR_SESION.getNombre()+Errors.NEGOCIO_CREDENCIALES_INCORRECTAS.getNombre());
		
		user.setClave("******");
		
		return user;
	}

	@Override
	@Transactional(transactionManager = "txManagerNegocio", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = {Exception.class, SQLException.class })
	public PerfilOpcions obtenerOpciones(String cuo, Integer idPerfil) throws Exception {
		PerfilOpcions perfilOpciones = accesoPersistencePort.obtenerOpciones(cuo, idPerfil);
		if(perfilOpciones.getOpciones().size()<1) 
			throw new ErrorException(Errors.DATOS_NO_ENCONTRADOS.getCodigo(), 
					Errors.ERROR_AL.getNombre()+Proceso.OBTENER_OPCIONES.getNombre()+Errors.NEGOCIO_PERFIL_NO_ENCONTRADO.getNombre());
		return perfilOpciones;
	}

}
