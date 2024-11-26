package pe.gob.pj.prueba.infraestructure.db.negocio.repositories;

import java.util.List;
import pe.gob.pj.prueba.domain.model.negocio.Persona;
import pe.gob.pj.prueba.domain.model.negocio.query.ConsultarPersonaQuery;  

public interface MovPersonaDslRepository {
  List<Persona> buscarPersona(ConsultarPersonaQuery query);
}
