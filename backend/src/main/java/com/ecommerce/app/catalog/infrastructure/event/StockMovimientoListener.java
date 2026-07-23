package com.ecommerce.app.catalog.infrastructure.event;

import com.ecommerce.app.catalog.application.service.MovimientoStockService;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.shared.domain.event.CompraRecibidaEvent;
import com.ecommerce.app.shared.domain.event.OrdenCanceladaEvent;
import com.ecommerce.app.shared.domain.event.OrdenCreadaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StockMovimientoListener {

    private final ProductoPort productoPort;
    private final MovimientoStockService movimientoStockService;

    public StockMovimientoListener(ProductoPort productoPort, MovimientoStockService movimientoStockService) {
        this.productoPort = productoPort;
        this.movimientoStockService = movimientoStockService;
    }

    @EventListener
    @Transactional
    public void onOrdenCreada(OrdenCreadaEvent event) {
        for (OrdenCreadaEvent.OrdenItemData item : event.items()) {
            Producto producto = productoPort.buscarPorId(item.productoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + item.productoId()));

            producto.reducirStock(item.cantidad());
            productoPort.guardar(producto);

            movimientoStockService.registrarSalidaPorOrden(
                    producto,
                    item.cantidad(),
                    event.ordenId(),
                    event.usuario()
            );
        }
    }

    @EventListener
    @Transactional
    public void onOrdenCancelada(OrdenCanceladaEvent event) {
        for (OrdenCanceladaEvent.OrdenItemData item : event.items()) {
            Producto producto = productoPort.buscarPorId(item.productoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + item.productoId()));

            producto.incrementarStock(item.cantidad());
            productoPort.guardar(producto);

            movimientoStockService.registrarDevolucion(
                    producto,
                    item.cantidad(),
                    event.ordenId(),
                    event.usuario(),
                    event.motivo()
            );
        }
    }

    @EventListener
    @Transactional
    public void onCompraRecibida(CompraRecibidaEvent event) {
        for (CompraRecibidaEvent.CompraItemData item : event.items()) {
            Producto producto = productoPort.buscarPorId(item.productoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + item.productoId()));

            producto.incrementarStock(item.cantidad());
            productoPort.guardar(producto);

            movimientoStockService.registrarEntradaPorCompra(
                    producto,
                    item.cantidad(),
                    event.compraId(),
                    event.usuario()
            );
        }
    }
}
