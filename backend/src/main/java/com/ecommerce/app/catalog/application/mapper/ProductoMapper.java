package com.ecommerce.app.catalog.application.mapper;

import com.ecommerce.app.catalog.application.dto.request.ActualizarProductoRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearProductoRequest;
import com.ecommerce.app.catalog.application.dto.response.ProductoBasicoResponse;
import com.ecommerce.app.catalog.application.dto.response.ProductoResponse;
import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoBasicoResponse toBasicoResponse(Producto producto) {
        if (producto == null) {
            return null;
        }

        return new ProductoBasicoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenNombre()
        );
    }
    
    public ProductoResponse toResponse(Producto producto) {
    	if (producto == null) {
    		return null;
    	}
    	
    	return new ProductoResponse(
    			producto.getId(),
    			producto.getNombre(),
    			producto.getSku(),
    			producto.getDescripcion(),
    			producto.getPrecio(),
    			producto.getStock(),
    			producto.getImagenNombre(),
    			producto.isActivo(),
    			producto.getCategoria().getId(),
    			producto.getCategoria().getNombre()
    	);
    }
    
    public Producto toEntity(CrearProductoRequest request, Categoria categoria) {
    	if (request == null) {
    		return null;
    	}
    	
    	Producto producto = new Producto();
    	producto.setNombre(request.nombre());
    	producto.setSku(request.sku());
    	producto.setDescripcion(request.descripcion());
    	producto.setPrecio(request.precio());
    	producto.setStock(0);
    	producto.setImagenNombre(request.imagenNombre());
    	producto.setActivo(true);
    	producto.setCategoria(categoria);
    	
    	return producto;    	
    }

	public void updateEntity(Producto producto, ActualizarProductoRequest request, Categoria categoria) {
		if (producto == null || request == null) {
			return;
		}

		producto.setNombre(request.nombre());
		producto.setSku(request.sku());
		producto.setDescripcion(request.descripcion());
		producto.setPrecio(request.precio());
		producto.setImagenNombre(request.imagenNombre());
		producto.setActivo(request.activo());
		producto.setCategoria(categoria);
	}
}