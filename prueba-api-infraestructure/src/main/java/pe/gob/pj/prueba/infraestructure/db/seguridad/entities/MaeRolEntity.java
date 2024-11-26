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
 * The persistent class for the mae_rol database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "mae_rol", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
public class MaeRolEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_rol")
  Integer nRol;

  @Column(name = "c_rol")
  String cRol;

  @Column(name = "x_descripcion")
  String xDescripcion;

  @Column(name = "x_rol")
  String xRol;

  // bi-directional many-to-one association to MaeOperacion
  @OneToMany(mappedBy = "maeRol")
  List<MaeOperacionEntity> maeOperacions;

  // bi-directional many-to-one association to MaeRolUsuario
  @OneToMany(mappedBy = "maeRol")
  List<MaeRolUsuarioEntity> maeRolUsuarios;

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
