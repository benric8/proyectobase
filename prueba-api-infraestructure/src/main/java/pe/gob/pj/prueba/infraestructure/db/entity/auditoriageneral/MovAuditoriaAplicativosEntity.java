package pe.gob.pj.prueba.infraestructure.db.entity.auditoriageneral;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.db.entity.AuditoriaEntity;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "MOV_AUDITORIA_APLICATIVOS", schema = ProjectConstants.Esquema.AUDITORIA_GENERAL)
public class MovAuditoriaAplicativosEntity extends AuditoriaEntity {
	/**
	* 
	*/
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_MOV_AUDITORIA_APLICATIVOS")
	@SequenceGenerator(name = "GEN_MOV_AUDITORIA_APLICATIVOS", schema = ProjectConstants.Esquema.AUDITORIA_GENERAL, 
		sequenceName = "USEQ_MOV_AUDITORIA_APLICATIVOS", initialValue = 1, allocationSize = 1)
	@Column(name = "N_AUDITORIA_APLICATIVO", nullable = false)
	Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_REGISTRO", nullable = false)
	Date fechaRegistro = new Date();

	@Column(name = "X_URI", nullable = false)
	String uri;
	
	@Column(name = "X_PETICION_URL", nullable = true)
	String peticionUrl;
	
	@Column(name = "X_PETICION_BODY", nullable = true, columnDefinition = "text")
	String peticionBody;
	
	@Column(name = "C_USUARIO_APLICATIVO", nullable = true)
	String usuarioAplicativo;
	
	@Column(name = "C_USUARIO_AUTH", nullable = false)
	String usuarioAuth;
	
	@Column(name = "X_IPS", nullable = false)
	String ips;
	
	@Column(name = "C_CUO_PETICION", nullable = false)
	String codigoUnicoOperacion;
	
	@Column(name = "C_RESPUESTA", nullable = false)
	String codigoRespuesta;
	
	@Column(name = "X_RESPUESTA", nullable = false)
	String descripcionRespuesta;
	
	@Column(name = "N_DURACION_RESPUESTA", nullable = false)
	Integer duracionRespuesta;
	
	@Column(name = "X_HERRAMIENTA_CONSUMO", nullable = true)
	String herramientaConsume;

}
