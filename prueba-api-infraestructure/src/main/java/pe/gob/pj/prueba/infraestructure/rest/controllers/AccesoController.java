package pe.gob.pj.prueba.infraestructure.rest.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.prueba.domain.model.auditoriageneral.PeticionServicios;
import pe.gob.pj.prueba.domain.model.negocio.query.IniciarSesionQuery;
import pe.gob.pj.prueba.domain.port.usecase.AccesoUseCasePort;
import pe.gob.pj.prueba.infraestructure.rest.requests.LoginRequest;
import pe.gob.pj.prueba.infraestructure.rest.requests.ObtenerOpcionesRequest;
import pe.gob.pj.prueba.infraestructure.rest.responses.PerfilOpcionesResponse;
import pe.gob.pj.prueba.infraestructure.rest.responses.UsuarioResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccesoController implements Acceso {

  @Qualifier("accesoUseCasePort")
  final AccesoUseCasePort accesoUC;

  @Override
  public ResponseEntity<UsuarioResponse> iniciarSesion(PeticionServicios peticion,
      @Valid LoginRequest request) {
    
    log.info("{} LOGIN [{}] [{}]", peticion.getCuo(), peticion.getIps(), peticion.getIp());

    var res = new UsuarioResponse();
    res.setCodigoOperacion(peticion.getCuo());

    res.setData(accesoUC.iniciarSesion(peticion.getCuo(),
        IniciarSesionQuery.builder().usuario(request.getUsuario()).clave(request.getClave())
            .aplicaCaptcha(request.getAplicaCaptcha()).tokenCaptcha(request.getTokenCaptcha())
            .build(),
        peticion));

    return new ResponseEntity<>(res, getHttpHeader(request.getFormatoRespuesta()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PerfilOpcionesResponse> obtenerOpciones(PeticionServicios peticion,
      @Valid ObtenerOpcionesRequest request) {
    log.info("{} OPCIONES [{}] [{}]", peticion.getCuo(), peticion.getIps(), peticion.getIp());
    var res = new PerfilOpcionesResponse();
    res.setCodigoOperacion(peticion.getCuo());
    res.setData(accesoUC.obtenerOpciones(peticion.getCuo(), request.getUsuario(),
        request.getIdPerfil(), peticion));

    return new ResponseEntity<>(res, getHttpHeader(request.getFormatoRespuesta()), HttpStatus.OK);
  }

}
