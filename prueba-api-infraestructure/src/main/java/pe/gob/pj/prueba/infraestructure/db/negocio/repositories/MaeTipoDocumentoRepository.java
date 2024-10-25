package pe.gob.pj.prueba.infraestructure.db.negocio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entities.MaeTipoDocumentoPersonaEntity;


public interface MaeTipoDocumentoRepository extends JpaRepository<MaeTipoDocumentoPersonaEntity, String> {

}
