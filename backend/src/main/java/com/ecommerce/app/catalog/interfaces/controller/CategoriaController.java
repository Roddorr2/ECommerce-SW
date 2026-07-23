package com.ecommerce.app.catalog.interfaces.controller;

import java.util.List;

import com.ecommerce.app.catalog.application.dto.request.ActualizarCategoriaRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearCategoriaRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.app.catalog.application.dto.response.CategoriaResponse;
import com.ecommerce.app.catalog.application.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/categorias")
@Tag(name = "Categoría", description = "Operaciones relacionadas con las categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener lista de categorías", description = "Devuelve todas las cateogrías de productos disponibles")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría específica según su ID")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable int id) {
        CategoriaResponse categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar una nueva categoría", description = "Registra una categoría en la base de datos")
    public ResponseEntity<CategoriaResponse> registrarCategoria(@RequestBody CrearCategoriaRequest request) {
        CategoriaResponse categoria = categoriaService.registrarCategoria(request);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar categoría", description = "Actualiza los datos de una categoría existente")
    public ResponseEntity<CategoriaResponse> editarCategoria(@RequestBody ActualizarCategoriaRequest request) {
        CategoriaResponse categoria = categoriaService.editarCategoria(request);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría no relacionada con productos")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable int id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Buscar una categoría por nombre", description = "Busca una categoría por su nombre")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNombre(@PathVariable String name) {
        List<CategoriaResponse> categorias = categoriaService.buscarPorNombre(name);
        return ResponseEntity.ok(categorias);
    }
    
}
