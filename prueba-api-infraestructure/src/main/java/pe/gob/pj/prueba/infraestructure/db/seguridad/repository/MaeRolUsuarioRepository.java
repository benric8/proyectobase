package pe.gob.pj.prueba.infraestructure.db.seguridad.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeRolUsuarioEntity;
import pe.gob.pj.prueba.infraestructure.db.seguridad.entity.MaeUsuarioEntity;

public interface MaeRolUsuarioRepository extends JpaRepository<MaeRolUsuarioEntity, Integer> {

  @Query(value = """
      SELECT u
      FROM MaeRolUsuarioEntity ru
      JOIN ru.maeRol r
      JOIN r.maeOperacions op
      JOIN op.maeAplicativo ap
      JOIN ru.maeUsuario u
      JOIN u.maeCliente c
      WHERE ru.activo = '1'
      AND u.activo = '1'
      AND r.activo = '1'
      AND ap.activo = '1'
      AND c.activo = '1'
      AND u.cUsuario = :cUsuario
      AND u.cClave = :cClave
      AND r.cRol = :codRol
      AND ap.nAplicativo = :nAplicativo
      AND c.cCliente = :cCliente
                """)
  Optional<MaeUsuarioEntity> autenticarUsuario(@Param("cUsuario") String usuario,
      @Param("cClave") String clave, @Param("codRol") String rol, @Param("cCliente") String cliente,
      @Param("nAplicativo") Integer aplicativo);

  @Query(value = """
      SELECT ru
      FROM MaeRolUsuarioEntity ru
      JOIN ru.maeRol r
      JOIN r.maeOperacions op
      JOIN ru.maeUsuario u
      WHERE u.activo = '1'
      AND ru.activo = '1'
      AND r.activo = '1'
      AND op.activo = '1'
      AND r.cRol = :codRol
      AND u.cUsuario = :cUsuario
      AND op.xEndpoint =:operacion
            """)
  Optional<MaeRolUsuarioEntity> obtenerAccesoMetodo(@Param("cUsuario") String usuario,
      @Param("codRol") String rol, @Param("operacion") String operacion);

}
