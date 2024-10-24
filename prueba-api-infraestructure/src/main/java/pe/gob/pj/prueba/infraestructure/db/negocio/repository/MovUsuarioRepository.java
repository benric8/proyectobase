package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MovUsuarioEntity;

public interface MovUsuarioRepository extends JpaRepository<MovUsuarioEntity, Integer> {

  Optional<MovUsuarioEntity> findByActivoAndUsuario(String activo, String usuario);
  
}
