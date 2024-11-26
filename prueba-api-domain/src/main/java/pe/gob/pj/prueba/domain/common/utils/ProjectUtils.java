package pe.gob.pj.prueba.domain.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectUtils {

  public static boolean isNullOrEmpty(Object valor) {
    boolean flag = false;
    if (valor == null || (String.valueOf(valor)).trim().equalsIgnoreCase("")
        || (String.valueOf(valor)).trim().equalsIgnoreCase("null")) {
      flag = true;
    }
    return flag;
  }

  public static String obtenerCodigoUnico() {
    Date fechaActual = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
    String strFechaActual = formato.format(fechaActual);
    Random random = new Random();
    int aleatorio = random.nextInt(999) + 1;
    StringBuilder cuo = new StringBuilder();
    cuo.append(strFechaActual).append(String.valueOf(aleatorio));
    return cuo.toString();
  }

  public static String convertDateToString(Date fecha, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    if (Objects.nonNull(fecha))
      return sdf.format(fecha);
    else
      return "";
  }

  public static Date sumarRestarSegundos(Date fecha, int segundos) {
    Calendar c = Calendar.getInstance();
    c.setTime(fecha);
    c.add(Calendar.SECOND, segundos);
    return c.getTime();
  }

  public static Date parseStringToDate(String fechaString, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.of("es", "ES"));
    Date fechaDate = null;
    try {
      fechaDate = simpleDateFormat.parse(fechaString);
    } catch (ParseException e) {
      log.error(" Error : {}", e);
    }
    return fechaDate;
  }

  public static String convertExceptionToString(Exception e) {
    String exception = "";
    if (e != null) {
      try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
        e.printStackTrace(pw);
        return sw.toString();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    } else {
      exception = "SE HA PRODUCIDO UNA EXCEPTION CUSTOMIZADA";
    }
    return exception;
  }



  public static String getPc() {
    String pc = null;
    try {
      InetAddress addr;
      addr = InetAddress.getLocalHost();
      pc = addr.getHostName();
    } catch (UnknownHostException e) {
      pc = "host";
    }

    return pc;
  }

  public static String getIp() {
    String ip = null;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      ip = "0.0.0.0";
    }

    return ip;
  }

  public static String getNombreRed() {
    String nombreRed = "Desconocido";
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress inetAddress = inetAddresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            nombreRed = networkInterface.getDisplayName();
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return nombreRed.length() < 30 ? nombreRed : nombreRed.substring(0, 29);
  }

  public static String getMac() {
    String firstInterface = null;
    Map<String, String> addressByNetwork = new HashMap<>();
    Enumeration<NetworkInterface> networkInterfaces = null;
    try {
      networkInterfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      e.printStackTrace();
    }
    if (networkInterfaces != null) {
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface network = networkInterfaces.nextElement();

        byte[] bmac = null;
        try {
          bmac = network.getHardwareAddress();
        } catch (SocketException e) {
          e.printStackTrace();
        }
        if (bmac != null) {
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < bmac.length; i++) {
            sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
          }

          if (sb.toString().isEmpty() == false) {
            addressByNetwork.put(network.getName(), sb.toString());

          }

          if (sb.toString().isEmpty() == false && firstInterface == null) {
            firstInterface = network.getName();
          }
        }
      }

      if (firstInterface != null) {
        return addressByNetwork.get(firstInterface);
      }
    }

    return firstInterface;
  }

}
