package com.ecommerce.app.catalog.application.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.app.catalog.application.dto.response.MovimientoStockResponse;
import com.ecommerce.app.catalog.application.mapper.MovimientoStockMapper;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.app.catalog.domain.enums.TipoMovimientoCodigo;
import com.ecommerce.app.catalog.domain.enums.TipoReferenciaCodigo;
import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.MovimientoStockPort;
import com.ecommerce.app.shared.domain.model.Usuario;

import jakarta.transaction.Transactional;

@Service
public class MovimientoStockService {
	
	private final MovimientoStockPort movimientoStockPort;
	private final ProductoPort productoPort;
	private final MovimientoStockMapper movimientoStockMapper;

	public MovimientoStockService(MovimientoStockPort movimientoStockPort,  ProductoPort productoPort, MovimientoStockMapper movimientoStockMapper) {
		this.movimientoStockPort = movimientoStockPort;
		this.productoPort = productoPort;
		this.movimientoStockMapper = movimientoStockMapper;
	}

	public List<MovimientoStockResponse> obtenerMovimientos(Integer id) {
		Producto producto = productoPort.buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));

		List<MovimientoStock> movimientos = movimientoStockPort
				.buscarPorProductoOrdenado(producto);

		return movimientos.stream()
				.map(movimientoStockMapper::toResponse)
				.toList();
	}
	
	@Transactional
	public MovimientoStock registrarSalidaPorOrden(Producto producto, int cantidad, String codigoOrden, Usuario usuario) {
		
		int stockAnterior = producto.getStock();

		MovimientoStock movimiento = new MovimientoStock();
		movimiento.setProducto(producto);
		movimiento.setCantidadAnterior(stockAnterior);
		movimiento.setCantidadNueva(stockAnterior - cantidad);
		movimiento.setTipoMovimiento(TipoMovimientoCodigo.SALIDA);
		movimiento.setTipoReferencia(TipoReferenciaCodigo.ORDEN);
		movimiento.setCodigoReferencia(codigoOrden);
		movimiento.setUsuario(usuario);
		movimiento.setFechaMovimiento(LocalDateTime.now());
		movimiento.setObservacion("Venta registrada - Orden #" + codigoOrden);
		
		movimiento.validarDatosCompletos();
		movimiento.validarConsistencia();
		
		return movimientoStockPort.guardar(movimiento);
	}
	
	@Transactional
	public MovimientoStock registrarEntradaPorCompra(Producto producto, int cantidad, String codigoCompra, Usuario usuario) {
		
		int stockAnterior = producto.getStock();
		
		MovimientoStock movimiento = new MovimientoStock();
		movimiento.setProducto(producto);
		movimiento.setCantidadAnterior(stockAnterior);
		movimiento.setCantidadNueva(stockAnterior + cantidad);
		movimiento.setTipoMovimiento(TipoMovimientoCodigo.ENTRADA);
		movimiento.setTipoReferencia(TipoReferenciaCodigo.COMPRA);
		movimiento.setCodigoReferencia(codigoCompra);
		movimiento.setUsuario(usuario);
		movimiento.setFechaMovimiento(LocalDateTime.now());
		movimiento.setObservacion("Compra recibida - Compra #" + codigoCompra);
		
		movimiento.validarDatosCompletos();
		movimiento.validarConsistencia();
		
		return movimientoStockPort.guardar(movimiento);		
	}
	
	@Transactional
	public MovimientoStock registrarDevolucion(Producto producto, int cantidad, String codigoOrden, Usuario usuario, String motivo) {
        
        int stockAnterior = producto.getStock();
        
        MovimientoStock movimiento = new MovimientoStock();
		movimiento.setProducto(producto);
		movimiento.setCantidadAnterior(stockAnterior);
		movimiento.setCantidadNueva(stockAnterior + cantidad);
		movimiento.setTipoMovimiento(TipoMovimientoCodigo.ENTRADA);
		movimiento.setTipoReferencia(TipoReferenciaCodigo.DEVOLUCION);
		movimiento.setCodigoReferencia(codigoOrden);
		movimiento.setUsuario(usuario);
		movimiento.setFechaMovimiento(LocalDateTime.now());
		movimiento.setObservacion("Devolución - Orden #" + codigoOrden + " - " + (motivo != null ? motivo : "Sin motivo"));
		
		movimiento.validarDatosCompletos();
		movimiento.validarConsistencia();
		
		return movimientoStockPort.guardar(movimiento);	
	}
}
