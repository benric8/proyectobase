package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MovUsuario;

public interface MovUsuarioRepository extends JpaRepository<MovUsuario, Integer> {

  Optional<MovUsuario> findByActivoAndUsuario(String activo, String usuario);
  
}
