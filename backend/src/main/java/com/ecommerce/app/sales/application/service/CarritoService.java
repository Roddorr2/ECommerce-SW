package com.ecommerce.app.sales.application.service;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.sales.application.dto.request.ActualizarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.request.AgregarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.CarritoResponse;
import com.ecommerce.app.sales.application.mapper.CarritoMapper;
import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.CarritoItem;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import com.ecommerce.app.sales.domain.port.CarritoItemPort;
import com.ecommerce.app.sales.domain.port.ClientePort;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoPort carritoPort;
    private final CarritoItemPort carritoItemPort;
    private final ClientePort clientePort;
    private final ProductoPort productoPort;
    private final CarritoMapper carritoMapper;

    public CarritoService(CarritoPort carritoPort, CarritoItemPort carritoItemPort, ClientePort clientePort, ProductoPort productoPort, CarritoMapper carritoMapper) {
        this.carritoPort = carritoPort;
        this.carritoItemPort = carritoItemPort;
        this.clientePort = clientePort;
        this.productoPort = productoPort;
        this.carritoMapper = carritoMapper;
    }

    @Transactional
    public CarritoResponse obtenerCarritoActivo(Integer clienteId) {
        Cliente cliente = clientePort.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Carrito carrito = carritoPort.buscarPorClienteYEstado(
                cliente, EstadoCarritoCodigo.ACTIVO
        ).orElseGet(() -> crearCarritoNuevo(cliente));

        return carritoMapper.toResponse(carrito);
    }

    @Transactional
    public CarritoResponse agregarItem(Integer clienteId, AgregarItemCarritoRequest request) {
        Cliente cliente = clientePort.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Carrito carrito = carritoPort.buscarPorClienteYEstado(
                cliente, EstadoCarritoCodigo.ACTIVO
        ).orElseGet(() -> crearCarritoNuevo(cliente));

        if (!carrito.puedeModificarse()) {
            throw new IllegalStateException("El carrito no permite modificaciones en este estado.");
        }

        Producto producto = productoPort.buscarPorId(request.productoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        if (!producto.isDisponible()) {
            throw new IllegalStateException("El producto no está disponible.");
        }

        Optional<CarritoItem> itemExistente = carritoItemPort
                .buscarPorCarritoYProducto(carrito, producto);

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + request.cantidad();

            if (!producto.tieneStockSuficiente(nuevaCantidad)) {
                throw new IllegalStateException("Stock insuficiente. Disponible: " + producto.getSku());
            }

            item.setCantidad(nuevaCantidad);
            carritoItemPort.guardar(item);
        } else {
            if (!producto.tieneStockSuficiente(request.cantidad())) {
                throw new IllegalStateException("Stock insuficiente. Disponible: " + producto.getSku());
            }

            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(request.cantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.setFechaAgregado(LocalDateTime.now());

            carritoItemPort.guardar(nuevoItem);
        }

        carrito.actualizarFecha();
        carritoPort.guardar(carrito);

        return carritoMapper.toResponse(carrito);
    }

    @Transactional
    public CarritoResponse actualizarItem(Integer clienteId, Integer itemId, ActualizarItemCarritoRequest request) {
        CarritoItem item = carritoItemPort.buscarPorId(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado."));

        if (!item.getCarrito().getCliente().getId().equals(clienteId)) {
            throw new IllegalStateException("Este ítem no pertenece al cliente.");
        }

        if (!item.getCarrito().puedeModificarse()) {
            throw new IllegalStateException("El carrito no permite modificaciones en este estado.");
        }

        item.validarCantidad(request.cantidad());

        if (!item.getProducto().tieneStockSuficiente(request.cantidad())) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        item.setCantidad(request.cantidad());
        carritoItemPort.guardar(item);

        Carrito carrito = item.getCarrito();
        carrito.actualizarFecha();
        carritoPort.guardar(carrito);

        return carritoMapper.toResponse(carrito);
    }

    @Transactional
    public CarritoResponse eliminarItem(Integer clienteId, Integer itemId) {
        CarritoItem item = carritoItemPort.buscarPorId(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado."));

        if (!item.getCarrito().getCliente().getId().equals(clienteId)) {
            throw new IllegalStateException("Este ítem no pertenece al cliente.");
        }
        
        if (!item.getCarrito().puedeModificarse()) {
			throw new IllegalStateException("El carrito no permite modificaciones en este estado.");
		}

        Carrito carrito = item.getCarrito();
        carritoItemPort.eliminar(item);
        carrito.actualizarFecha();
        carritoPort.guardar(carrito);
        
        return carritoMapper.toResponse(carrito);
    }
    
    @Transactional
    public void vaciarCarrito(Integer clienteId) {
    	Cliente cliente = clientePort.buscarPorId(clienteId)
    			.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."));
    	
    	Carrito carrito = carritoPort.buscarPorClienteYEstado(
    			cliente, EstadoCarritoCodigo.ACTIVO
    	).orElseThrow(() -> new RuntimeException("No hay carrito activo."));
    	
    	if (!carrito.puedeModificarse()) {
    		throw new IllegalStateException("El carrito no permite modificaciones en este estado.");
    	}
    	
    	carritoItemPort.eliminarTodos(carrito.getItems());
    	
    	carrito.actualizarFecha();
    	carritoPort.guardar(carrito);
    }
    
    private Carrito crearCarritoNuevo(Cliente cliente) {
    	
    	Carrito nuevoCarrito = new Carrito();
    	nuevoCarrito.setCliente(cliente);
    	nuevoCarrito.setEstadoCarrito(EstadoCarritoCodigo.ACTIVO);
    	nuevoCarrito.setFechaCreacion(LocalDateTime.now());
    	nuevoCarrito.setFechaActualizacion(LocalDateTime.now());
    	
    	return carritoPort.guardar(nuevoCarrito);
    }
}
