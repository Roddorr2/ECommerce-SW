package com.ecommerce.app.auth.application.service;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.auth.domain.port.CodigoVerificacionPort;
import com.ecommerce.app.shared.domain.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CodigoVerificacionService {

    private final CodigoVerificacionPort codigoPort;

    private static final int DURACION_CODIGO_MINUTOS = 10;
    private static final int MAX_CODIGOS_POR_HORA = 3;

    public CodigoVerificacionService(CodigoVerificacionPort codigoPort) {
        this.codigoPort = codigoPort;
    }

    @Transactional
    public CodigoVerificacion generarCodigo(Usuario usuario) {
        validarRateLimiting(usuario);
        invalidarCodigosPendientes(usuario);
        String codigo = generarCodigoAleatorio();

        CodigoVerificacion codigoVerificacion = new CodigoVerificacion();
        codigoVerificacion.setUsuario(usuario);
        codigoVerificacion.setCodigo(codigo);
        codigoVerificacion.setEstado(EstadoCodigoVerificacionCodigo.PENDIENTE);
        codigoVerificacion.setFechaGeneracion(LocalDateTime.now());
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(DURACION_CODIGO_MINUTOS));
        codigoVerificacion.setIntentosRealizados(0);
        codigoVerificacion.setIntentosMaximos(3);

        return codigoPort.guardar(codigoVerificacion);
    }

    @Transactional
    public boolean validarCodigo(Usuario usuario, String codigo) {
        CodigoVerificacion codigoVerificacion = codigoPort
                .buscarPorUsuarioYCodigoYEstado(usuario, codigo, EstadoCodigoVerificacionCodigo.PENDIENTE)
                .orElseThrow(() -> new RuntimeException("Código inválido o expirado."));

        if (!codigoVerificacion.puedeIntentar()) {
            throw new RuntimeException("Código bloqueado por intentos fallidos.");
        }

        if (codigoVerificacion.getCodigo().equals(codigo) && codigoVerificacion.estaVigente()) {
            codigoVerificacion.validarPuedeMarcarComoUsado();
            codigoVerificacion.setEstado(EstadoCodigoVerificacionCodigo.USADO);
            codigoVerificacion.setFechaUso(LocalDateTime.now());
            codigoPort.guardar(codigoVerificacion);
            return true;

        } else {
            codigoVerificacion.registrarIntentoFallido();

            if (codigoVerificacion.getIntentosRealizados() >= codigoVerificacion.getIntentosMaximos()) {
                codigoVerificacion.setEstado(EstadoCodigoVerificacionCodigo.BLOQUEADO);
            }

            codigoPort.guardar(codigoVerificacion);
            return false;
        }
    }

    private void validarRateLimiting(Usuario usuario) {
        LocalDateTime haceUnaHora = LocalDateTime.now().minusHours(1);
        long codigosRecientes = codigoPort.contarPorUsuarioYFechaGeneracionDespues(usuario, haceUnaHora);

        if (codigosRecientes >= MAX_CODIGOS_POR_HORA) {
            throw new RuntimeException("Has solicitado demasiados códigos, Intenta en una hora.");
        }
    }

    private void invalidarCodigosPendientes(Usuario usuario) {
        List<CodigoVerificacion> codigosPendientes = codigoPort
                .buscarPorUsuarioYEstado(usuario, EstadoCodigoVerificacionCodigo.PENDIENTE);

        codigosPendientes.forEach(c -> c.setEstado(EstadoCodigoVerificacionCodigo.EXPIRADO));
        codigoPort.guardarTodos(codigosPendientes);
    }

    private String generarCodigoAleatorio() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }
}
