package pe.gob.pj.prueba.infraestructure.db.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.port.persistence.GestionPersonaPersistencePort;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.db.entity.servicio.MaeTipoDocumentoPersona;
import pe.gob.pj.prueba.infraestructure.db.entity.servicio.MovPersona;
import pe.gob.pj.prueba.infraestructure.enums.Estado;

@Component("gestionPersonaPersistencePort")
public class GestionPersonaPersistenceAdapter implements GestionPersonaPersistencePort{

	@Autowired
	@Qualifier("sessionNegocio")
	private SessionFactory sf;
	
	@Override
	public List<Persona> buscarPersona(String cuo, Map<String, Object> filters) throws Exception {
		List<Persona> lista = new ArrayList<>();
		if(!ProjectUtils.isNullOrEmpty(filters.get(Persona.P_NUMERO_DOCUMENTO))) {
			this.sf.getCurrentSession().enableFilter(MovPersona.F_DOCUMENTO_IDENTIDAD)
				.setParameter(MovPersona.P_DOCUMENTO_IDENTIDAD, filters.get(Persona.P_NUMERO_DOCUMENTO));
		}
		TypedQuery<MovPersona> query = this.sf.getCurrentSession().createNamedQuery(MovPersona.Q_ALL, MovPersona.class);
		query.getResultStream().forEach(movUsuario -> {
			Persona personaDto = new Persona();
			personaDto.setId(movUsuario.getId());
			personaDto.setPrimerApellido(movUsuario.getPrimerApellido());
			personaDto.setSegundoApellido(movUsuario.getSegundoApellido());
			personaDto.setNombres(movUsuario.getNombres());
			personaDto.setNumeroDocumento(movUsuario.getNumeroDocumento());
			personaDto.setTelefono(movUsuario.getTelefono());
			personaDto.setCorreo(movUsuario.getCorreo());
			personaDto.setIdTipoDocumento(movUsuario.getTipoDocumento().getCodigo());
			personaDto.setTipoDocumento(movUsuario.getTipoDocumento().getAbreviatura());
			personaDto.setFechaNacimiento(ProjectUtils.convertDateToString(movUsuario.getFechaNacimiento(), ProjectConstants.Formato.FECHA_DD_MM_YYYY));
			personaDto.setSexo(movUsuario.getSexo());
			personaDto.setActivo(movUsuario.getActivo());
			lista.add(personaDto);
		});
		return lista;
	}

	@Override
	public void registrarPersona(String cuo, Persona persona) throws Exception {
		MaeTipoDocumentoPersona maeTipoDocumento = new MaeTipoDocumentoPersona();
		maeTipoDocumento.setCodigo(persona.getIdTipoDocumento());
		MovPersona movPersona = new MovPersona();
		movPersona.setTipoDocumento(maeTipoDocumento);
		movPersona.setNumeroDocumento(persona.getNumeroDocumento());
		movPersona.setPrimerApellido(persona.getPrimerApellido());
		movPersona.setSegundoApellido(persona.getSegundoApellido());
		movPersona.setNombres(persona.getNombres());
		movPersona.setSexo(persona.getSexo());
		movPersona.setCorreo(persona.getCorreo());
		movPersona.setTelefono(persona.getTelefono());
		movPersona.setFechaNacimiento(ProjectUtils.parseStringToDate(persona.getFechaNacimiento(), ProjectConstants.Formato.FECHA_DD_MM_YYYY));
		movPersona.setActivo(!Estado.INACTIVO_NUMERICO.getNombre().equals(persona.getActivo())?Estado.ACTIVO_NUMERICO.getNombre() : Estado.INACTIVO_NUMERICO.getNombre());
		this.sf.getCurrentSession().save(movPersona);
		persona.setId(movPersona.getId());
	}

	@Override
	public void actualizarPersona(String cuo, Persona persona) throws Exception {
		this.sf.getCurrentSession().enableFilter(MovPersona.F_ID)
			.setParameter(MovPersona.P_ID, persona.getId());
		TypedQuery<MovPersona> query = this.sf.getCurrentSession().createNamedQuery(MovPersona.Q_ALL, MovPersona.class);
		MovPersona movPersona = query.getSingleResult();
		if(!movPersona.getTipoDocumento().getCodigo().equals(persona.getIdTipoDocumento())) {
			MaeTipoDocumentoPersona maeTipoDocumento = new MaeTipoDocumentoPersona();
			maeTipoDocumento.setCodigo(persona.getIdTipoDocumento());
			movPersona.setTipoDocumento(maeTipoDocumento);
		}
		movPersona.setNumeroDocumento(persona.getNumeroDocumento());
		movPersona.setPrimerApellido(persona.getPrimerApellido());
		movPersona.setSegundoApellido(persona.getSegundoApellido());
		movPersona.setNombres(persona.getNombres());
		movPersona.setSexo(persona.getSexo());
		movPersona.setCorreo(persona.getCorreo());
		movPersona.setTelefono(persona.getTelefono());
		movPersona.setFechaNacimiento(ProjectUtils.parseStringToDate(persona.getFechaNacimiento(), ProjectConstants.Formato.FECHA_DD_MM_YYYY));
		movPersona.setActivo(!Estado.INACTIVO_NUMERICO.getNombre().equals(persona.getActivo())?Estado.ACTIVO_NUMERICO.getNombre() : Estado.INACTIVO_NUMERICO.getNombre());
		this.sf.getCurrentSession().update(movPersona);
	}

}
