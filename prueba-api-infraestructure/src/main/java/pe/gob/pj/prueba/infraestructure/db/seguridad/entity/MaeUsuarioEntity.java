package pe.gob.pj.prueba.infraestructure.db.seguridad.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.enums.Estado;
import pe.gob.pj.prueba.infraestructure.enums.OperacionBaseDato;


/**
 * The persistent class for the mae_usuario database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name="mae_usuario", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
@NamedQuery(name="MaeUsuario.findAll", query="SELECT m FROM MaeUsuarioEntity m")
@NamedQuery(name=MaeUsuarioEntity.FIND_BY_ID, query="SELECT m FROM MaeUsuarioEntity m WHERE m.activo = '1' AND m.nUsuario = :idUsuario ")
@NamedQuery(name=MaeUsuarioEntity.FIND_BY_CODIGO, query="SELECT m FROM MaeUsuarioEntity m WHERE m.activo = '1' AND m.cUsuario = :codigo ")
public class MaeUsuarioEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_ID = "MaeUsuario.buscarPorId";
	public static final String P_N_USUARIO = "idUsuario";
	public static final String FIND_BY_CODIGO = "MaeUsuario.buscarPorcodigo";
	public static final String P_N_CODIGO = "codigo";

	@Id
	@Column(name="n_usuario")
	private Integer nUsuario;

	@Column(name="c_clave")
	private String cClave;

	@Column(name="c_usuario")
	private String cUsuario;

	@Column(name="f_registro")
	private Timestamp fRegistro;

	//bi-directional many-to-one association to MaeRolUsuario
	@OneToMany(mappedBy="maeUsuario")
	private List<MaeRolUsuarioEntity> maeRolUsuarios;

	//bi-directional many-to-one association to MaeCliente
	@ManyToOne
	@JoinColumn(name="n_cliente")
	private MaeClienteEntity maeCliente;
    
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