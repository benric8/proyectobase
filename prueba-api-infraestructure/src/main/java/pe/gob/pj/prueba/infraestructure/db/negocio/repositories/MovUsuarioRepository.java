package pe.gob.pj.prueba.infraestructure.db.negocio.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MovUsuarioEntity;

public interface MovUsuarioRepository extends JpaRepository<MovUsuarioEntity, Integer> {

  Optional<MovUsuarioEntity> findByActivoAndUsuario(String activo, String usuario);
  
}
