package com.ecommerce.app.catalog.application.service;

import com.ecommerce.app.catalog.application.dto.request.ActualizarProductoRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearProductoRequest;
import com.ecommerce.app.catalog.application.dto.response.ProductoImagenResponse;
import com.ecommerce.app.catalog.application.dto.response.ProductoResponse;
import com.ecommerce.app.catalog.application.mapper.ProductoMapper;
import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.catalog.domain.port.CategoriaPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoPort productoPort;
    private final CategoriaPort categoriaPort;
    private final String CARPETA_IMAGENES = Paths.get(System.getProperty("user.dir"), "wwwroot", "imagenes").toString();
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoPort productoPort, CategoriaPort categoriaPort, ProductoMapper productoMapper) {
        this.productoPort = productoPort;
        this.categoriaPort = categoriaPort;
        this.productoMapper = productoMapper;
    }

    public List<ProductoResponse> listarProductos() {
        return productoPort.listarTodos()
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductoResponse obtenerPorId(int id) {
        Producto producto = productoPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID " + id));
        return productoMapper.toResponse(producto);
    }

    @Transactional
    public ProductoResponse registrarProducto(CrearProductoRequest request) {
        Categoria categoria = categoriaPort.buscarPorId(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID " + request.categoriaId()));

        if (productoPort.buscarPorSku(request.sku()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el SKU: " + request.sku());
        }

        Producto producto = productoMapper.toEntity(request, categoria);
        Producto productoGuardado = productoPort.guardar((producto));

        return productoMapper.toResponse(productoGuardado);
    }

    @Transactional
    public ProductoResponse editarProducto(ActualizarProductoRequest request) {
        Producto producto = productoPort.buscarPorId(request.id())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el producto con ID " + request.id()));

        Categoria categoria = categoriaPort.buscarPorId(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID " + request.categoriaId()));

        productoPort.buscarPorSku(request.sku()).ifPresent(p -> {
            if (!p.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro producto con el SKU: " + request.sku());
            }
        });

        productoMapper.updateEntity(producto, request, categoria);
        Producto productoActualizado = productoPort.guardar(producto);

        return productoMapper.toResponse(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(int id) {
        Producto producto = productoPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (productoPort.existeEnCompras(id)) {
            throw new IllegalStateException("No se puede eliminar un producto asociado a una compra");
        }

        if (productoPort.existeEnOrdenes(id)) {
            throw new IllegalStateException("No se puede eliminar un producto asociado a una orden");
        }

        productoPort.eliminar(producto);
    }

    public ProductoImagenResponse subirImagen(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Archivo no válido");
        }

        File dir = new File(CARPETA_IMAGENES);
        if (!dir.exists()) dir.mkdirs();

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.isEmpty()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }

        String extension = "";
        int i = nombreOriginal.lastIndexOf('.');
        if (i > 0) {
            extension = nombreOriginal.substring(i); 
            nombreOriginal = nombreOriginal.substring(0, i);
        }

        nombreOriginal = nombreOriginal.toLowerCase()
            .replaceAll("\\s+", "_")
            .replaceAll("[^a-z0-9_-]", "");

        String nombreArchivo = UUID.randomUUID() + "-" + nombreOriginal + extension;

        String rutaCompleta = Paths.get(CARPETA_IMAGENES, nombreArchivo).toString();
        archivo.transferTo(new File(rutaCompleta));

        String url = "/imagenes/" + nombreArchivo;
        return new ProductoImagenResponse(nombreArchivo, url);
    }

    
    public List<ProductoResponse> buscarProductosDisponibles(String nombre) {
    	return productoPort.buscarProductosDisponibles(nombre)
    			.stream()
    			.map(productoMapper::toResponse)
    			.toList();
    }
}
