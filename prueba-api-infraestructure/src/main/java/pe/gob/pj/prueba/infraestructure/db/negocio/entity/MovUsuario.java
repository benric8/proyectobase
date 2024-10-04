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
@Table(name="MOV_USUARIO", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value= {
		@NamedQuery(name=MovUsuario.Q_ALL, query = "SELECT mu FROM MovUsuario mu")
})
@FilterDefs(value= {
		@FilterDef(name=MovUsuario.F_ACCESO, parameters = {@ParamDef(name=MovUsuario.P_ACTIVO, type = String.class), @ParamDef(name=MovUsuario.P_USUARIO,type = String.class)}),
		@FilterDef(name=MovUsuario.F_ID, parameters = {@ParamDef(name=MovUsuario.P_ID, type = Integer.class)}),
		@FilterDef(name=MovUsuario.F_USUARIO, parameters = {@ParamDef(name=MovUsuario.P_USUARIO, type = String.class)})
})
@Filters(value= {
		@Filter(name=MovUsuario.F_ACCESO, condition = "L_ACTIVO=:"+MovUsuario.P_ACTIVO+" AND X_USUARIO=:"+MovUsuario.P_USUARIO),
		@Filter(name=MovUsuario.F_ID, condition = "N_USUARIO=:"+MovUsuario.P_ID),
		@Filter(name=MovUsuario.F_USUARIO, condition = "X_USUARIO=:"+MovUsuario.P_USUARIO)
})
public class MovUsuario implements Serializable {/**
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
