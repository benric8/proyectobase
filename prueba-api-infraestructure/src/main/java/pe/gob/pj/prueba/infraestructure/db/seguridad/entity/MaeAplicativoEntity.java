package pe.gob.pj.prueba.infraestructure.db.seguridad.entity;

import java.io.Serializable;
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
 * The persistent class for the mae_aplicativo database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "mae_aplicativo", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
@NamedQuery(name = "MaeAplicativo.findAll", query = "SELECT m FROM MaeAplicativoEntity m")
public class MaeAplicativoEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_aplicativo")
  private Integer nAplicativo;

  @Column(name = "c_aplicativo")
  private String cAplicativo;

  @Column(name = "x_aplicativo")
  private String xAplicativo;

  @Column(name = "x_descripcion")
  private String xDescripcion;

  // bi-directional many-to-one association to MaeTipoAplicativo
  @ManyToOne
  @JoinColumn(name = "n_tipo_aplicativo")
  private MaeTipoAplicativoEntity maeTipoAplicativo;

  // bi-directional many-to-one association to MaeOperacion
  @OneToMany(mappedBy = "maeAplicativo")
  private List<MaeOperacionEntity> maeOperacions;

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
