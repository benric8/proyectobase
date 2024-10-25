package pe.gob.pj.prueba.infraestructure.db.negocio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MovPersonaEntity;


public interface MovPersonaRepository
    extends JpaRepository<MovPersonaEntity, Integer>, MovPersonaDslRepository {

}
