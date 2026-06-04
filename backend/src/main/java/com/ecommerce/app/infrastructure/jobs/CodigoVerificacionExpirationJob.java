package com.ecommerce.app.infrastructure.jobs;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.auth.domain.port.CodigoVerificacionPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CodigoVerificacionExpirationJob {

    private static final Logger log = LoggerFactory.getLogger(CodigoVerificacionExpirationJob.class);

    private final CodigoVerificacionPort codigoPort;

    public CodigoVerificacionExpirationJob(CodigoVerificacionPort codigoPort) {
        this.codigoPort = codigoPort;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void expirarCodigos() {
        List<CodigoVerificacion> expirados = codigoPort
                .buscarCodigosExpirados(EstadoCodigoVerificacionCodigo.PENDIENTE, LocalDateTime.now());

        if (expirados.isEmpty()) return;

        expirados.forEach(c -> c.setEstado(EstadoCodigoVerificacionCodigo.EXPIRADO));
        codigoPort.guardarTodos(expirados);

        log.info("Códigos expirados procesados {}", expirados.size());
    }
}
