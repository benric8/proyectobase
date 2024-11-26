package pe.gob.pj.prueba.infraestructure.rest.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pe.gob.pj.prueba.infraestructure.common.enums.FormatoRespuesta;

public interface Base {

  default HttpHeaders getHttpHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
    return headers;
  }

  default HttpHeaders getHttpHeader(String formatoRespuesta) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(
        MediaType.parseMediaType(FormatoRespuesta.XML.getNombre().equalsIgnoreCase(formatoRespuesta)
            ? MediaType.APPLICATION_XML_VALUE
            : MediaType.APPLICATION_JSON_VALUE));
    return headers;
  }

}
