package pe.gob.pj.prueba.infraestructure.db.negocio.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.common.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.common.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.common.enums.OperacionBaseDatos;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "MAE_TIPO_DOCUMENTO_PERSONA", schema = ProjectConstants.Esquema.PRUEBA)
public class MaeTipoDocumentoPersonaEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "B_TIPO_DOCUMENTO_PERSONA", nullable = false, length = 1)
  String codigo;
  @Column(name = "X_ABREVIATURA", nullable = false)
  String abreviatura;

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
