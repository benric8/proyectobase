package pe.gob.pj.prueba.infraestructure.db.seguridad.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.common.utils.ProjectUtils;
import pe.gob.pj.prueba.domain.common.utils.SecurityConstants;
import pe.gob.pj.prueba.infraestructure.common.enums.Estado;
import pe.gob.pj.prueba.infraestructure.common.enums.OperacionBaseDatos;



/**
 * The persistent class for the mae_operacion database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "mae_operacion", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
public class MaeOperacionEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @Column(name = "n_operacion")
  Integer nOperacion;

  @Column(name = "x_descripcion")
  String xDescripcion;

  @Column(name = "x_endpoint")
  String xEndpoint;

  @Column(name = "x_operacion")
  String xOperacion;

  // bi-directional many-to-one association to MaeAplicativo
  @ManyToOne
  @JoinColumn(name = "n_aplicativo")
  MaeAplicativoEntity maeAplicativo;

  // bi-directional many-to-one association to MaeRol
  @ManyToOne
  @JoinColumn(name = "n_rol")
  MaeRolEntity maeRol;

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
