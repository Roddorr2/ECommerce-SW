package com.ecommerce.app.infrastructure.jobs;

import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CarritoExpirationJob {

    private static final Logger log = LoggerFactory.getLogger(CarritoExpirationJob.class);

    private final CarritoPort carritoPort;

    public CarritoExpirationJob(CarritoPort carritoPort) {
        this.carritoPort = carritoPort;
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void expirarCarritos() {
        LocalDateTime limite = LocalDateTime.now().minusDays(7);

        List<Carrito> abandonados = carritoPort
                .buscarExpirados(EstadoCarritoCodigo.ACTIVO, limite);

        if (abandonados.isEmpty()) return;

        abandonados.forEach(c -> c.setEstadoCarrito(EstadoCarritoCodigo.ABANDONADO));
        carritoPort.guardarTodos(abandonados);

        log.info("Carritos marcados como abandonados: {}", abandonados.size());
    }
}
