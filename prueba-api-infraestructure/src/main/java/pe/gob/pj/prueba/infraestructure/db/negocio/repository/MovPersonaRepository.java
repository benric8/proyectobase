package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MovPersona;

public interface MovPersonaRepository extends JpaRepository<MovPersona, Integer>, MovPersonaDslRepository {

}
