package com.ecommerce.app.catalog.interfaces.controller;

import com.ecommerce.app.catalog.application.dto.response.MovimientoStockResponse;
import com.ecommerce.app.catalog.application.service.MovimientoStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movimientos-stock")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Movimientos de stock", description = "Endpoints para consultradr historial de movimientos")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    public MovimientoStockController(MovimientoStockService movimientoStockService) {
        this.movimientoStockService = movimientoStockService;
    }

    @GetMapping("/producto/{productoId}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener movimientos de un producto", description = "Retorna el historial de movimientos de stock de un producto específico")
    public ResponseEntity<List<MovimientoStockResponse>> obtenerMovimientosPorProducto(@PathVariable Integer productoId) {
        return ResponseEntity.ok(movimientoStockService.obtenerMovimientos(productoId));
    }
}
