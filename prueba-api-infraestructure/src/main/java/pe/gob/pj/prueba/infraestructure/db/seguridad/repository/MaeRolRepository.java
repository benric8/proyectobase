package pe.gob.pj.prueba.infraestructure.db.seguridad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeRolEntity;

public interface MaeRolRepository extends JpaRepository<MaeRolEntity, Integer> {

  List<MaeRolEntity> findByActivoAndMaeRolUsuariosMaeUsuarioNusuario(String activo, Integer nUsuario);

}
