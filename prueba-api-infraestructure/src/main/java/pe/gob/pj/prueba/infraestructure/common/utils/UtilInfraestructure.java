package pe.gob.pj.prueba.infraestructure.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.infraestructure.common.enums.ErrorDefault;
import pe.gob.pj.prueba.infraestructure.common.enums.TipoError;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilInfraestructure {

  public static void handleException(String cuo, Exception e, TipoError tipoError) {
    log.error("{} {} | {} | {} | {} | {} | {}", cuo, ErrorDefault.TRAZA.getNombre(),
        tipoError.getCodigo(), tipoError.getDescripcion(),
        UtilInfraestructure.obtenerClaseMetodoLineaException(e), e.getMessage(),
        UtilInfraestructure.obtenerCausaException(e));
  }

  public static String obtenerCausaException(Exception e) {
    int causaRaiz = 10;
    StringBuilder causaString = new StringBuilder();
    Throwable causa = e.getCause();
    for (int i = 0; i < causaRaiz; i++) {
      if (causa != null) {
        causaString.append(" ").append(causa);
        causa = causa.getCause();
      } else
        break;

    }
    return causaString.toString().trim().equalsIgnoreCase("")
        ? ErrorDefault.CAUSA_NO_IDENTIFICADA.getNombre()
        : causaString.toString();
  }

  public static String obtenerClaseMetodoLineaException(Exception e) {
    String claseMetodoLineaString = "";
    StackTraceElement[] stackTrace = e.getStackTrace();
    if (stackTrace != null && stackTrace.length > 0) {
      StackTraceElement firstElement = stackTrace[0];
      claseMetodoLineaString = firstElement.getClassName() + "::" + firstElement.getMethodName()
          + "::" + firstElement.getLineNumber();
    }
    return claseMetodoLineaString.trim().equalsIgnoreCase("")
        ? ErrorDefault.CLASE_METODO_LINEA_NO_IDENTIFICADO.getNombre()
        : claseMetodoLineaString;
  }

}
