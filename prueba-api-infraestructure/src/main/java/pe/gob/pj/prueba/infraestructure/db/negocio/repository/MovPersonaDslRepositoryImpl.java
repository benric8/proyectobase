package pe.gob.pj.prueba.infraestructure.db.negocio.repository;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pe.gob.pj.prueba.domain.model.servicio.Persona;
import pe.gob.pj.prueba.domain.query.ConsultarPersonaQuery;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.QMaeTipoDocumentoPersona;
import pe.gob.pj.prueba.infraestructure.db.negocio.entity.QMovPersona;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MovPersonaDslRepositoryImpl implements MovPersonaDslRepository {

  @Qualifier("negocioQDSL")
  private final JPAQueryFactory queryFactory;

  @Override
  public List<Persona> buscarPersona(ConsultarPersonaQuery query) {

    var persona = QMovPersona.movPersona;
    var tipoDocumento = QMaeTipoDocumentoPersona.maeTipoDocumentoPersona;

    var filtro = new BooleanBuilder();

    if (Objects.nonNull(query.documentoIdentidad())) {
      filtro.and(persona.numeroDocumento.eq(query.documentoIdentidad()));
    }
    
    return queryFactory
        .select(Projections.constructor(Persona.class, persona.id, persona.numeroDocumento,
            persona.fechaNacimiento.stringValue(), persona.primerApellido, persona.segundoApellido,
            persona.nombres, persona.sexo, persona.correo, persona.telefono, persona.activo,
            tipoDocumento.codigo, tipoDocumento.abreviatura))
        .from(persona).innerJoin(persona.tipoDocumento, tipoDocumento).where(filtro).fetch();
  }

}
