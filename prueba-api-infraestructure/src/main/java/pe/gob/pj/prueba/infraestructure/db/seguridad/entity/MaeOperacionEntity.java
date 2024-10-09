package pe.gob.pj.prueba.infraestructure.db.seguridad.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * The persistent class for the mae_operacion database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "mae_operacion", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
public class MaeOperacionEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_operacion")
  private Integer nOperacion;

  @Column(name = "x_descripcion")
  private String xDescripcion;

  @Column(name = "x_endpoint")
  private String xEndpoint;

  @Column(name = "x_operacion")
  private String xOperacion;

  // bi-directional many-to-one association to MaeAplicativo
  @ManyToOne
  @JoinColumn(name = "n_aplicativo")
  private MaeAplicativoEntity maeAplicativo;

  // bi-directional many-to-one association to MaeRol
  @ManyToOne
  @JoinColumn(name = "n_rol")
  private MaeRolEntity maeRol;

  // Auditoria
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "F_AUD")
  private Date fAud = new Date();
  @Column(name = "B_AUD")
  private String bAud = OperacionBaseDato.INSERTAR.getNombre();
  @Column(name = "C_AUD_UID")
  private String cAudId;
  @Column(name = "C_AUD_UIDRED")
  private String cAudIdRed = ProjectUtils.getNombreRed();
  @Column(name = "C_AUD_PC")
  private String cAudPc = ProjectUtils.getPc();
  @Column(name = "C_AUD_IP")
  private String cAudIp = ProjectUtils.getIp();
  @Column(name = "C_AUD_MCADDR")
  private String cAudMcAddr = ProjectUtils.getMac();
  @Column(name = "L_ACTIVO", length = 1, nullable = false)
  private String activo = Estado.ACTIVO_NUMERICO.getNombre();

}
