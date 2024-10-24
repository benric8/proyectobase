package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import java.util.List;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.model.servicio.query.ConsultarPersonaQuery;  

public interface MovPersonaDslRepository {
  List<Persona> buscarPersona(ConsultarPersonaQuery query);
}
