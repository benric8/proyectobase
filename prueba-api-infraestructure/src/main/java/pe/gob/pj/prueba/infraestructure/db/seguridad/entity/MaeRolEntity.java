package pe.gob.pj.prueba.infraestructure.db.seguridad.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
 * The persistent class for the mae_rol database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name="mae_rol", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
@NamedQuery(name="MaeRol.findAll", query="SELECT m FROM MaeRolEntity m")
@NamedQuery(name=MaeRolEntity.FIND_ROLES_BY_ID_USUARIO, query="SELECT m FROM MaeRolEntity m JOIN m.maeRolUsuarios ur WHERE m.activo = '1' AND ur.maeUsuario.nUsuario = :idUsuario")
public class MaeRolEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ROLES_BY_ID_USUARIO = "MaeRol.rolesPorUsuario";

	@Id
	@Column(name="n_rol")
	private Integer nRol;

	@Column(name="c_rol")
	private String cRol;

	@Column(name="x_descripcion")
	private String xDescripcion;

	@Column(name="x_rol")
	private String xRol;

	//bi-directional many-to-one association to MaeOperacion
	@OneToMany(mappedBy="maeRol")
	private List<MaeOperacionEntity> maeOperacions;

	//bi-directional many-to-one association to MaeRolUsuario
	@OneToMany(mappedBy="maeRol")
	private List<MaeRolUsuarioEntity> maeRolUsuarios;
    
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