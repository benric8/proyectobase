package pe.gob.pj.prueba.infraestructure.db.negocio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MaePerfilEntity;

public interface MaePerfilRepository extends JpaRepository<MaePerfilEntity, Integer> {

}
