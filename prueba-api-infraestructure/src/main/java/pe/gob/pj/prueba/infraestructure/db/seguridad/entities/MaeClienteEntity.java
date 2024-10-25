package pe.gob.pj.prueba.infraestructure.db.seguridad.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.common.enums.OperacionBaseDato;


/**
 * The persistent class for the mae_cliente database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "mae_cliente", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
public class MaeClienteEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_cliente")
  private Integer nCliente;

  @Column(name = "c_cliente")
  private String cCliente;

  @Column(name = "l_tipo_cliente")
  private String lTipoCliente;

  @Column(name = "x_cliente")
  private String xCliente;

  @Column(name = "x_descripcion")
  private String xDescripcion;

  // bi-directional many-to-one association to MaeUsuario
  @OneToMany(mappedBy = "maeCliente")
  private List<MaeUsuarioEntity> maeUsuarios;

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
