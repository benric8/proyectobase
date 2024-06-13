package pe.gob.pj.prueba.infraestructure.db.entity.servicio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.gob.pj.prueba.domain.utils.ProjectConstants;
import pe.gob.pj.prueba.infraestructure.db.entity.AuditoriaEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="MAE_TIPO_DOCUMENTO_PERSONA", schema = ProjectConstants.Esquema.PRUEBA)
@NamedQueries(value = {
		@NamedQuery(name = MaeTipoDocumentoPersona.Q_ALL, query = "SELECT mtdp FROM MaeTipoDocumentoPersona mtdp")
})
@Entity
public class MaeTipoDocumentoPersona extends AuditoriaEntity implements Serializable{/**
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

}
