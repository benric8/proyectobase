package pe.gob.pj.prueba.infraestructure.db.seguridad.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.common.enums.OperacionBaseDatos;


/**
 * The persistent class for the mae_tipo_aplicativo database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "mae_tipo_aplicativo", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
public class MaeTipoAplicativoEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_tipo_aplicativo")
  Integer nTipoAplicativo;

  @Column(name = "x_descripcion")
  String xDescripcion;

  @Column(name = "x_tipo_aplicativo")
  String xTipoAplicativo;

  // bi-directional many-to-one association to MaeAplicativo
  @OneToMany(mappedBy = "maeTipoAplicativo")
  List<MaeAplicativoEntity> maeAplicativos;

  // Auditoria
  @Column(name = "F_AUD")
  LocalDateTime fAud = LocalDateTime.now();
  @Column(name = "B_AUD")
  String bAud = OperacionBaseDatos.INSERTAR.getNombre();
  @Column(name = "C_AUD_UID")
  String cAudId;
  @Column(name = "C_AUD_UIDRED")
  String cAudIdRed = ProjectUtils.getNombreRed();
  @Column(name = "C_AUD_PC")
  String cAudPc = ProjectUtils.getPc();
  @Column(name = "C_AUD_IP")
  String cAudIp = ProjectUtils.getIp();
  @Column(name = "C_AUD_MCADDR")
  String cAudMcAddr = ProjectUtils.getMac();
  @Column(name = "L_ACTIVO", length = 1, nullable = false)
  String activo = Estado.ACTIVO_NUMERICO.getNombre();

}
