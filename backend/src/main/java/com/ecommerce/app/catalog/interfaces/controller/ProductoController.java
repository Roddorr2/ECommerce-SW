package com.ecommerce.app.catalog.interfaces.controller;

import com.ecommerce.app.catalog.application.dto.request.ActualizarProductoRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearProductoRequest;
import com.ecommerce.app.catalog.application.dto.response.ProductoImagenResponse;
import com.ecommerce.app.catalog.application.dto.response.ProductoResponse;
import com.ecommerce.app.catalog.application.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Obtener lista de productos", description = "Devuelve todos los productos registrados en el sistema")
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        List<ProductoResponse> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto según su identificador único")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable int id) {
        ProductoResponse producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo producto", description = "Registra un nuevo producto en la base de datos")
    public ResponseEntity<ProductoResponse> registrarProducto(@RequestBody CrearProductoRequest request) {
        ProductoResponse producto = productoService.registrarProducto(request);
        return ResponseEntity.ok(producto);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar producto", description = "Actualiza los datos de un producto existente")
    public ResponseEntity<ProductoResponse> editarProducto(@RequestBody ActualizarProductoRequest request) {
        ProductoResponse producto = productoService.editarProducto(request);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto no relacionado con compras u órdenes")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/imagenes")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Subir imagen de producto", description = "Sube una imagen para un producto y devuelve su DTO de respuesta")
    public ResponseEntity<ProductoImagenResponse> subirImagen(@RequestParam("archivo") MultipartFile archivo) throws IOException {
        ProductoImagenResponse respuesta = productoService.subirImagen(archivo);
        return ResponseEntity.ok(respuesta);
    }
    
    @GetMapping("/search/{name}")
    @PreAuthorize("permitAll()") 
    @Operation(summary = "Busca productos disponibles", description = "Busca los productos que estén disponibles")
    public ResponseEntity<List<ProductoResponse>> buscarProductosDisponibles(@PathVariable String name) {
        List<ProductoResponse> productos = productoService.buscarProductosDisponibles(name);
    	return ResponseEntity.ok(productos);
    }
}
