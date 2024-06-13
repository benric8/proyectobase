package pe.gob.pj.prueba.infraestructure.db.entity.servicio;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.db.entity.AuditoriaEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="MOV_USUARIO_PERFIL", schema = ProjectConstants.Esquema.PRUEBA)
@Entity
public class MovUsuarioPerfil extends AuditoriaEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_MOV_USUARIO_PERFIL", schema = ProjectConstants.Esquema.PRUEBA, sequenceName = "USEQ_MOV_USUARIO_PERFIL" , initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOV_USUARIO_PERFIL")
	@Column(name="N_USUARIO_PERFIL", nullable = false)
	private Integer id;
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name="N_USUARIO")
	private MovUsuario usuario;
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name="N_PERFIL")
	private MaePerfil perfil;
	
}
