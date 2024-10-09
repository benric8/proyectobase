package pe.gob.pj.prueba.infraestructure.db.negocio.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.enums.Estado;
import pe.gob.pj.prueba.infraestructure.enums.OperacionBaseDato;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="MAE_TIPO_DOCUMENTO_PERSONA", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value = {
		@NamedQuery(name = MaeTipoDocumentoPersona.Q_ALL, query = "SELECT mtdp FROM MaeTipoDocumentoPersona mtdp")
})
@Entity
public class MaeTipoDocumentoPersona implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String Q_ALL = "MaeTipoDocumentoPersona.q.all";

	public static final String TABLA = "MAE_TIPO_DOCUMENTO_PERSONA";
	public static final String COLUMNA_NOMBRE = "X_NOMBRE";	
	
	@Id
	@Column(name="B_TIPO_DOCUMENTO_PERSONA", nullable = false, length = 1)
	private String codigo;
	@Column(name="X_ABREVIATURA", nullable = false)
	private String abreviatura;
    
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
