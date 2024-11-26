package pe.gob.pj.prueba.infraestructure.db.negocio.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "MOV_PERSONA", schema = ProjectConstants.Esquema.PRUEBA)
public class MovPersonaEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "SEQ_MOV_PERSONA", schema = ProjectConstants.Esquema.PRUEBA,
      sequenceName = "USEQ_MOV_PERSONA", initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOV_PERSONA")
  @Column(name = "N_PERSONA", nullable = false)
  Integer id;
  @Column(name = "X_DOCUMENTO_IDENTIDAD", nullable = false)
  String numeroDocumento;
  @Column(name = "F_NACIMIENTO", nullable = false)
  Date fechaNacimiento;
  @Column(name = "X_PRIMER_APELLIDO", nullable = false)
  String primerApellido;
  @Column(name = "X_SEGUNDO_APELLIDO", nullable = false)
  String segundoApellido;
  @Column(name = "X_NOMBRES", nullable = false)
  String nombres;
  @Column(name = "L_SEXO", nullable = false)
  String sexo;
  @Column(name = "X_CORREO", nullable = true)
  String correo;
  @Column(name = "X_TELEFONO", nullable = true)
  String telefono;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "B_TIPO_DOCUMENTO_PERSONA")
  MaeTipoDocumentoPersonaEntity tipoDocumento;

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
