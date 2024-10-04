package pe.gob.pj.prueba.infraestructure.db.negocio.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.enums.Estado;
import pe.gob.pj.prueba.infraestructure.enums.OperacionBaseDato;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="MAE_OPCION", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value= {
		@NamedQuery(name=MaeOpcion.Q_ALL, query="SELECT mo FROM MaeOpcion mo")
})
@FilterDefs(value= {
		@FilterDef(name=MaeOpcion.F_ID, parameters = {@ParamDef(name=MaeOpcion.P_ID, type = Integer.class)}),
		@FilterDef(name=MaeOpcion.F_ID_SUPERIOR, parameters = {@ParamDef(name=MaeOpcion.P_ID_SUPERIOR, type = Integer.class)})
})
@Filters(value= {
		@Filter(name=MaeOpcion.F_ID, condition = "N_OPCION=:"+MaeOpcion.P_ID),
		@Filter(name=MaeOpcion.F_ID_SUPERIOR, condition = "N_OPCION_SUPERIOR=:"+MaeOpcion.P_ID_SUPERIOR)
})
public class MaeOpcion implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String Q_ALL = "MaeOpcion.q.all";
	
	public static final String F_ID = "MaeOpcion.f.id";
	public static final String F_ID_SUPERIOR = "MaeOpcion.f.idSuperior";
	
	public static final String P_ID = "idMaeOpcion";
	public static final String P_ID_SUPERIOR = "idSuperiorMaeOpcion";
	
	public static final String TABLA = "MAE_OPCION";
	public static final String COLUMNA_NOMBRE = "X_NOMBRE";
	public static final String COLUMNA_DESCRIPCION = "X_DESCRIPCION";

	@Id
	@SequenceGenerator(name="SEQ_MAE_OPCION", schema = ProjectConstants.Esquema.PRUEBA, sequenceName = "USEQ_MAE_OPCION" , initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MAE_OPCION")
	@Column(name="N_OPCION", nullable = false)
	private Integer id;
	@Column(name="C_OPCION", nullable = false)
	private String codigo;
	@Column(name="X_NOMBRE", nullable = false)
	private String nombre;
	@Column(name="X_URL", nullable = false)
	private String url;
	@Column(name="X_ICONO", nullable = false)
	private String icono;
	@Column(name="N_ORDEN", nullable = false)
	private Integer orden;
	
	@ManyToOne(optional = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name="N_OPCION_SUPERIOR")
	private MaeOpcion opcionSuperior;
	
	@OneToMany(mappedBy = "opcion", fetch = FetchType.LAZY)
	private List<MovOpcionPerfil> perfils = new ArrayList<MovOpcionPerfil>();
    
    //Auditoria
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="F_AUD")
    private Date fAud = new Date();
    @Column(name="B_AUD")
    private String bAud = OperacionBaseDato.INSERTAR.getNombre();
    @Column(name="C_AUD_UID")
    private String cAudId;
    @Column(name="C_AUD_UIDRED")
    private String cAudIdRed = ProjectUtils.getNombreRed();
    @Column(name="C_AUD_PC")
    private String cAudPc = ProjectUtils.getPc();
    @Column(name="C_AUD_IP")
    private String cAudIp = ProjectUtils.getIp();
    @Column(name="C_AUD_MCADDR")
    private String cAudMcAddr = ProjectUtils.getMac();
    @Column(name = "L_ACTIVO", length = 1, nullable = false)
    private String activo = Estado.ACTIVO_NUMERICO.getNombre();

}
