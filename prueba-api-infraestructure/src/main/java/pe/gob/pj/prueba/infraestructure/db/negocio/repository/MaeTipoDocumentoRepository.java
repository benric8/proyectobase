package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.MaeTipoDocumentoPersona;


public interface MaeTipoDocumentoRepository extends JpaRepository<MaeTipoDocumentoPersona, String> {

}
