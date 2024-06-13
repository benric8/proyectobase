package pe.gob.pj.prueba.infraestructure.db.entity.servicio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.db.entity.AuditoriaEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="MOV_USUARIO", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value= {
		@NamedQuery(name=MovUsuario.Q_ALL, query = "SELECT mu FROM MovUsuario mu")
})
@FilterDefs(value= {
		@FilterDef(name=MovUsuario.F_ACCESO, parameters = {@ParamDef(name=MovUsuario.P_ACTIVO, type = "string"), @ParamDef(name=MovUsuario.P_USUARIO,type = "string")}),
		@FilterDef(name=MovUsuario.F_ID, parameters = {@ParamDef(name=MovUsuario.P_ID, type = "integer")}),
		@FilterDef(name=MovUsuario.F_USUARIO, parameters = {@ParamDef(name=MovUsuario.P_USUARIO, type = "string")})
})
@Filters(value= {
		@Filter(name=MovUsuario.F_ACCESO, condition = "L_ACTIVO=:"+MovUsuario.P_ACTIVO+" AND X_USUARIO=:"+MovUsuario.P_USUARIO),
		@Filter(name=MovUsuario.F_ID, condition = "N_USUARIO=:"+MovUsuario.P_ID),
		@Filter(name=MovUsuario.F_USUARIO, condition = "X_USUARIO=:"+MovUsuario.P_USUARIO)
})
public class MovUsuario extends AuditoriaEntity implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String Q_ALL = "movUsuario.q.all";
	
	public static final String F_ACCESO = "MovUsuario.f.acceso";
	public static final String F_ID = "MovUsuario.f.id";
	public static final String F_ACTIVO = "MovUsuario.f.activo";
	public static final String F_USUARIO = "MovUsuario.f.usuario";
	
	public static final String P_ID = "idMovUsuario";
	public static final String P_ACTIVO = "activoMovUsuario";
	public static final String P_USUARIO = "usuarioMovUsuario";

	@Id
	@SequenceGenerator(name="SEQ_MOV_USUARIO", schema = ProjectConstants.Esquema.PRUEBA, sequenceName = "USEQ_MOV_USUARIO" , initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOV_USUARIO")
	@Column(name="N_USUARIO", nullable = false)
	private Integer id;
	@Column(name="X_USUARIO", nullable = false)
	private String usuario;
	@Column(name="X_CLAVE", nullable = false)
	private String clave;
	
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name="N_PERSONA")
	private MovPersona persona;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<MovUsuarioPerfil> perfils = new ArrayList<MovUsuarioPerfil>();
	
}
