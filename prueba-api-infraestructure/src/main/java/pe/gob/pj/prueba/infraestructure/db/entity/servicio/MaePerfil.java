package pe.gob.pj.prueba.infraestructure.db.entity.servicio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="MAE_PERFIL", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value= {
		@NamedQuery(name=MaePerfil.Q_ALL, query = "SELECT mp FROM MaePerfil mp")
})
@FilterDefs(value= {
		@FilterDef(name=MaePerfil.F_ID, parameters = {@ParamDef(name=MaePerfil.P_ID, type = "integer")}),
		@FilterDef(name=MaePerfil.F_ACTIVO, parameters = {@ParamDef(name=MaePerfil.P_ACTIVO, type = "string")})
})
@Filters(value= {
		@Filter(name=MaePerfil.F_ID, condition = "N_PERFIL=:"+MaePerfil.P_ID),
		@Filter(name=MaePerfil.F_ACTIVO, condition = "L_ACTIVO=:"+MaePerfil.P_ACTIVO)
})
public class MaePerfil extends AuditoriaEntity implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String Q_ALL = "MaePerfil.q.all";
	
	public static final String F_ID = "MaePerfil.f.id";
	public static final String F_ACTIVO = "MaePerfil.f.activo";
	
	public static final String P_ID = "idMaePerfil";
	public static final String P_ACTIVO = "activoMaePerfil";

	@Id
	@SequenceGenerator(name="SEQ_MAE_PERFIL", schema = ProjectConstants.Esquema.PRUEBA, sequenceName = "USEQ_MAE_PERFIL" , initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MAE_PERFIL")
	@Column(name="N_PERFIL", nullable = false)
	private Integer id;
	@Column(name="X_NOMBRE", nullable = false)
	private String nombre;
	@Column(name="C_ROL", nullable = false)
	private String rol;
	
	@OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY)
	private List<MovOpcionPerfil> perfilsOpcion = new ArrayList<MovOpcionPerfil>();
	
}