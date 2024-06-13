package pe.gob.pj.prueba.domain.model.seguridad;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Ramesh Fadatare
 *
 */
@NoArgsConstructor
@Data
@Entity
@Table(name="roles")
public class RoleSecurity
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true)
	private String name;
		
	@ManyToMany(mappedBy="roles")
	private List<UserSecurity> users;

	public RoleSecurity(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
