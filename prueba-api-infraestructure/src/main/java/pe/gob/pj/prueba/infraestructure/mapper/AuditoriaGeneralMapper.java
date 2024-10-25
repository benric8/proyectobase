package pe.gob.pj.prueba.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pe.gob.pj.prueba.domain.model.auditoriageneral.AuditoriaAplicativos;
import pe.gob.pj.prueba.infraestructure.db.auditoriageneral.entities.MovAuditoriaAplicativosEntity;
import pe.gob.pj.prueba.infraestructure.rest.requests.AuditoriaRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditoriaGeneralMapper {

  @Mapping(target = "usuarioAplicativo", source = "auditoriaRequest.usuario")
  @Mapping(target = "usuario", source = "auditoriaRequest.usuario")
  @Mapping(target = "nombrePc", source = "auditoriaRequest.nombrePc")
  @Mapping(target = "numeroIp", source = "auditoriaRequest.numeroIp")
  @Mapping(target = "direccionMac", source = "auditoriaRequest.direccionMac")
  @Mapping(target = "fechaRegistro", expression = "java(new java.util.Date())")
  @Mapping(target = "codigoUnicoOperacion", source = "cuo")
  @Mapping(target = "ips", source = "ips")
  @Mapping(target = "usuarioAuth", source = "usuauth")
  @Mapping(target = "uri", source = "uri")
  @Mapping(target = "peticionUrl", source = "peticionUrl")
  @Mapping(target = "herramientaConsume", source = "herramientaConsume")
  @Mapping(target = "codigoRespuesta", source = "codigoRespuesta")
  @Mapping(target = "descripcionRespuesta", source = "descripcionRespuesta")
  @Mapping(target = "duracionRespuesta", source = "tiempo")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "peticionBody", ignore = true)
  AuditoriaAplicativos toAuditoriaAplicativos(AuditoriaRequest auditoriaRequest, String cuo,
      String ips, String usuauth, String uri, String peticionUrl, String herramientaConsume,
      String codigoRespuesta, String descripcionRespuesta, long tiempo);

  @Mapping(target = "CAudId", source = "usuarioAuth")
  @Mapping(target = "activo", ignore = true)
  @Mapping(target = "BAud", ignore = true)
  @Mapping(target = "CAudIdRed", ignore = true)
  @Mapping(target = "CAudIp", ignore = true)
  @Mapping(target = "CAudMcAddr", ignore = true)
  @Mapping(target = "CAudPc", ignore = true)
  @Mapping(target = "FAud", ignore = true)
  MovAuditoriaAplicativosEntity toMovAuditoriaAplicativos(
      AuditoriaAplicativos auditoriaAplicativos);

}
