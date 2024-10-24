package pe.gob.pj.prueba.infraestructure.db.negocio.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.domain.utils.ProjectUtils;
import pe.gob.pj.prueba.infraestructure.enums.Estado;
import pe.gob.pj.prueba.infraestructure.enums.OperacionBaseDato;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "MOV_USUARIO", schema = ProjectConstants.Esquema.PRUEBA)
public class MovUsuarioEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "SEQ_MOV_USUARIO", schema = ProjectConstants.Esquema.PRUEBA,
      sequenceName = "USEQ_MOV_USUARIO", initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOV_USUARIO")
  @Column(name = "N_USUARIO", nullable = false)
  Integer id;
  @Column(name = "X_USUARIO", nullable = false)
  String usuario;
  @Column(name = "X_CLAVE", nullable = false)
  String clave;

  @ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name = "N_PERSONA")
  MovPersonaEntity persona;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  List<MovUsuarioPerfilEntity> perfils = new ArrayList<MovUsuarioPerfilEntity>();

  // Auditoria
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "F_AUD")
  Date fAud = new Date();
  @Column(name = "B_AUD")
  String bAud = OperacionBaseDato.INSERTAR.getNombre();
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
