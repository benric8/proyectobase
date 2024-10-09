package pe.gob.pj.prueba.infraestructure.db.seguridad.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeUsuarioEntity;

public interface MaeUsuarioRepository extends JpaRepository<MaeUsuarioEntity, Integer> {

  Optional<MaeUsuarioEntity> findByNusuarioAndActivo(Integer id, String activo);

}
