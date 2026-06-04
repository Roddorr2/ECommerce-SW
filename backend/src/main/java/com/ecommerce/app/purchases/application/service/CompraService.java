package com.ecommerce.app.purchases.application.service;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.purchases.application.dto.request.CambiarEstadoCompraRequest;
import com.ecommerce.app.purchases.application.dto.request.CrearCompraRequest;
import com.ecommerce.app.purchases.application.dto.response.CompraResponse;
import com.ecommerce.app.purchases.application.dto.response.CompraResumenResponse;
import com.ecommerce.app.purchases.application.mapper.CompraMapper;
import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.port.CompraPort;
import com.ecommerce.app.purchases.domain.port.ProveedorPort;
import com.ecommerce.app.shared.domain.event.CompraRecibidaEvent;
import com.ecommerce.app.shared.domain.model.Empleado;
import com.ecommerce.app.shared.domain.port.EmpleadoPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraService {

    private final CompraPort compraPort;
    private final ProveedorPort proveedorPort;
    private final EmpleadoPort empleadoPort;
    private final ProductoPort productoPort;
    private final CompraMapper compraMapper;
    private final ApplicationEventPublisher eventPublisher;

    public CompraService(CompraPort compraPort, ProveedorPort proveedorPort, EmpleadoPort empleadoPort, ProductoPort productoPort, CompraMapper compraMapper, ApplicationEventPublisher eventPublisher) {
        this.compraPort = compraPort;
        this.proveedorPort = proveedorPort;
        this.empleadoPort = empleadoPort;
        this.productoPort = productoPort;
        this.compraMapper = compraMapper;
        this.eventPublisher = eventPublisher;
    }

    public List<CompraResumenResponse> listarCompras() {
        return compraPort.listarTodas().stream()
                .map(compraMapper::toResumenResponse)
                .toList();
    }

    public CompraResponse obtenerPorId(int id) {
        Compra compra = compraPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + id));
        return compraMapper.toResponse(compra);
    }

    @Transactional
    public CompraResponse crearCompra(CrearCompraRequest request) {
        Proveedor proveedor = proveedorPort.buscarPorId(request.proveedorId())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + request.proveedorId()));

        Empleado empleado = empleadoPort.findById(request.empleadoId())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + request.empleadoId()));

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setEmpleado(empleado);
        compra.setEstadoCompra(EstadoCompraCodigo.PENDIENTE);
        compra.setFechaCompra(LocalDate.now());

        for (var item : request.items()) {
            Producto producto = productoPort.buscarPorId(item.productoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + item.productoId()));

            CompraDetalle detalle = new CompraDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(item.precioUnitario());
            compra.agregarDetalle(detalle);
        }

        compra.validarParaConfirmacion();
        Compra savedCompra = compraPort.guardar(compra);
        return compraMapper.toResponse(savedCompra);
    }

    @Transactional
    public CompraResponse recibirCompra(Integer compraId) {
        Compra compra = compraPort.buscarPorId(compraId)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + compraId));

        if (!compra.puedeRecibirse()) {
            throw new IllegalStateException("Esta compra no puede recibirse. Estado actual: " + compra.getEstadoCompra().name());
        }

        compra.transicionarA(EstadoCompraCodigo.RECIBIDA);
        Compra savedCompra = compraPort.guardar(compra);

        List<CompraRecibidaEvent.CompraItemData> eventItems = savedCompra.getDetalles().stream()
                .map(d -> new CompraRecibidaEvent.CompraItemData(d.getProducto().getId(), d.getCantidad()))
                .toList();

        eventPublisher.publishEvent(new CompraRecibidaEvent(
                savedCompra.getId().toString(),
                savedCompra.getEmpleado().getUsuario(),
                eventItems
        ));

        return compraMapper.toResponse(savedCompra);
    }

    @Transactional
    public CompraResponse cancelarCompra(Integer compraId, String motivo) {
        Compra compra = compraPort.buscarPorId(compraId)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + compraId));

        if (!compra.puedeCancelarse()) {
            throw new IllegalStateException("Esta compra no puede cancelarse. Estado actual: " + compra.getEstadoCompra().name());
        }

        compra.transicionarA(EstadoCompraCodigo.CANCELADA);
        Compra savedCompra = compraPort.guardar(compra);
        return compraMapper.toResponse(savedCompra);
    }

    @Transactional
    public CompraResponse cambiarEstado(Integer compraId, CambiarEstadoCompraRequest request) {
        if (request.codigo() == EstadoCompraCodigo.RECIBIDA) {
            return recibirCompra(compraId);
        }
        if (request.codigo() == EstadoCompraCodigo.CANCELADA) {
            return cancelarCompra(compraId, "Cambio de estado manual");
        }

        Compra compra = compraPort.buscarPorId(compraId)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + compraId));

        compra.transicionarA(request.codigo());
        Compra savedCompra = compraPort.guardar(compra);
        return compraMapper.toResponse(savedCompra);
    }

    public List<CompraResumenResponse> obtenerComprasPorProveedor(Integer proveedorId) {
        Proveedor proveedor = proveedorPort.buscarPorId(proveedorId)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + proveedorId));

        return compraPort.buscarPorProveedorOrdenado(proveedor).stream()
                .map(compraMapper::toResumenResponse)
                .toList();
    }

    public List<CompraResumenResponse> obtenerComprasPorEstado(EstadoCompraCodigo codigoEstado) {
        return compraPort.buscarPorEstadoOrdenado(codigoEstado).stream()
                .map(compraMapper::toResumenResponse)
                .toList();
    }

    @Transactional
    public void eliminarCompra(int id) {
        Compra compra = compraPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID " + id));

        if (compra.isRecibida()) {
            throw new IllegalStateException("No se puede eliminar una compra que ya fue recibida (stock ya incrementado)");
        }

        compraPort.eliminar(compra);
    }
}
