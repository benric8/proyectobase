package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MaePerfilEntity;

public interface MaePerfilRepository extends JpaRepository<MaePerfilEntity, Integer> {

}
