package pe.gob.pj.prueba.infraestructure.db.seguridad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.pj.prueba.domain.model.seguridad.Rol;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeRolEntity;

public interface MaeRolRepository extends JpaRepository<MaeRolEntity, Integer> {

  @Query(value = """
      SELECT
          new pe.gob.pj.prueba.domain.model.seguridad.Rol(m.nRol,m.cRol,m.xRol,m.activo)
      FROM MaeRolEntity m
      JOIN m.maeRolUsuarios ur
      WHERE m.activo = :activo
      AND ur.maeUsuario.nUsuario = :idUsuario
      """)
  List<Rol> obtenerRolesUsuario(@Param("activo") String activo,
      @Param("idUsuario") Integer idUsuario);

}
