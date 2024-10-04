package pe.gob.pj.prueba.infraestructure.db.seguridad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeUsuarioEntity;

public interface MaeUsuarioRepository extends JpaRepository<MaeUsuarioEntity, Integer>{
  
  MaeUsuarioEntity findBynUsuarioAndActivo(Integer id, String activo);

}
