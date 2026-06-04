package com.ecommerce.app.catalog.domain.port;

import com.ecommerce.app.catalog.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaPort {
    Optional<Categoria> buscarPorId(Integer id);
    Optional<Categoria> buscarPorNombre(String nombre);
    List<Categoria> buscarPorNombreConteniendo(String nombre);
    Categoria guardar(Categoria categoria);
    void eliminar(Categoria categoria);
    List<Categoria> listarTodas();
}
