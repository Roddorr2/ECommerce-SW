package com.ecommerce.app.sales.application.service;

import com.ecommerce.app.sales.application.dto.request.CambiarEstadoOrdenRequest;
import com.ecommerce.app.sales.application.dto.request.CrearOrdenDesdeCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.OrdenResponse;
import com.ecommerce.app.sales.application.dto.response.OrdenResumenResponse;
import com.ecommerce.app.sales.application.mapper.OrdenMapper;
import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.domain.model.*;
import com.ecommerce.app.sales.domain.port.OrdenPort;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.sales.domain.port.MetodoPagoPort;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import com.ecommerce.app.shared.domain.event.OrdenCanceladaEvent;
import com.ecommerce.app.shared.domain.event.OrdenCreadaEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenPort ordenPort;
    private final ClientePort clientePort;
    private final MetodoPagoPort metodoPagoPort;
    private final CarritoPort carritoPort;
    private final OrdenMapper ordenMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OrdenService(OrdenPort ordenPort, ClientePort clientePort, MetodoPagoPort metodoPagoPort, CarritoPort carritoPort, OrdenMapper ordenMapper, ApplicationEventPublisher eventPublisher) {
        this.ordenPort = ordenPort;
        this.clientePort = clientePort;
        this.metodoPagoPort = metodoPagoPort;
        this.carritoPort = carritoPort;
        this.ordenMapper = ordenMapper;
        this.eventPublisher = eventPublisher;
    }

    public OrdenResponse crearOrdenDesdeCarrito(Integer clienteId, CrearOrdenDesdeCarritoRequest request) {
        Cliente cliente = clientePort.buscarPorId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."));

        Carrito carrito = carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo para este cliente"));

        carrito.validarParaConversion();

        validarStockDisponible(carrito.getItems());

        MetodoPago metodoPago = metodoPagoPort.buscarPorId(request.metodoPagoId())
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado."));

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setMetodoPago(metodoPago);
        orden.setEstadoOrden(EstadoOrdenCodigo.PENDIENTE);
        orden.setFechaOrden(LocalDateTime.now());
        orden.establecerDireccionEnvio(request.direccionEnvio());

        for (CarritoItem item : carrito.getItems()) {
            OrdenDetalle detalle = new OrdenDetalle();
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            orden.agregarDetalle(detalle);
        }

        orden.validarParaConfirmacion();

        Orden ordenGuardada = ordenPort.guardar(orden);

        List<OrdenCreadaEvent.OrdenItemData> eventItems = orden.getDetalles().stream()
                .map(d -> new OrdenCreadaEvent.OrdenItemData(d.getProducto().getId(), d.getCantidad()))
                .toList();

        eventPublisher.publishEvent(new OrdenCreadaEvent(
                ordenGuardada.getId().toString(),
                cliente.getUsuario(),
                eventItems
        ));

        carrito.setEstadoCarrito(EstadoCarritoCodigo.CONVERTIDO);
        carrito.actualizarFecha();
        carritoPort.guardar(carrito);

        return ordenMapper.toResponse(ordenGuardada);
    }

    private void validarStockDisponible(List<CarritoItem> items) {
        List<String> productosAgotados = new ArrayList<>();

        for (CarritoItem item : items) {
            if (!item.getProducto().tieneStockSuficiente(item.getCantidad())) {
                productosAgotados.add(item.getProducto().getNombre());
            }
        }

        if (!productosAgotados.isEmpty()) {
            throw new IllegalStateException("Stock insuficiente para: " + String.join(" ,", productosAgotados));
        }
    }

    @Transactional
    public OrdenResponse cambiarEstado(Integer ordenId, CambiarEstadoOrdenRequest request) {
        Orden orden = ordenPort.buscarPorId(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        orden.transicionarA(request.codigo());

        return ordenMapper.toResponse(ordenPort.guardar(orden));
    }

    @Transactional
    public OrdenResponse cancelarOrden(Integer ordenId, String motivo) {
        Orden orden = ordenPort.buscarPorId(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada."));

        if (!orden.puedeCancelarse()) {
            throw new IllegalStateException("Esta orden no puede cancelarse.");
        }

        List<OrdenCanceladaEvent.OrdenItemData> eventItems = orden.getDetalles().stream()
                .map(d -> new OrdenCanceladaEvent.OrdenItemData(d.getProducto().getId(), d.getCantidad()))
                .toList();

        eventPublisher.publishEvent(new OrdenCanceladaEvent(
                orden.getId().toString(),
                orden.getCliente().getUsuario(),
                motivo,
                eventItems
        ));

        orden.setEstadoOrden(EstadoOrdenCodigo.PENDIENTE);
        Orden ordenCancelada = ordenPort.guardar(orden);

        return ordenMapper.toResponse(ordenCancelada);
    }

    public List<OrdenResumenResponse> listarOrdenes() {
        return ordenPort.listarTodas().stream()
                .map(ordenMapper::toResumenResponse)
                .toList();
    }

    public List<OrdenResumenResponse> obtenerOrdenesPorCliente(Integer clienteId) {
        Cliente cliente = clientePort.buscarPorId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        List<Orden> ordenes = ordenPort.buscarPorClienteOrdenado(cliente);

        return ordenes.stream()
                .map(ordenMapper::toResumenResponse)
                .toList();
    }

    public List<OrdenResumenResponse> obtenerOrdenesPorEstado(EstadoOrdenCodigo codigo) {
        return ordenPort.buscarPorEstadoOrdenado(codigo)
                .stream().map(ordenMapper::toResumenResponse).toList();
    }

    public OrdenResponse obtenerPorId(Integer ordenId) {
        Orden orden = ordenPort.buscarPorId(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        return ordenMapper.toResponse(orden);
    }
}
