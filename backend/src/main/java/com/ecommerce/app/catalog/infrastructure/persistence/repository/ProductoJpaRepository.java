package com.ecommerce.app.catalog.infrastructure.persistence.repository;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Integer> {
    @Query("""
			SELECT p 
			FROM ProductoEntity p 
			WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) 
			AND p.stock > 0
		""")
    List<ProductoEntity> buscarProductosDisponibles(@Param("nombre") String nombre);

    Optional<ProductoEntity> findBySku(String sku);

    @Query("SELECT COUNT(c) > 0 FROM CompraDetalleEntity c WHERE c.productoEntity.id = :id")
    boolean existeEnCompras(@Param("id") Integer id);

    @Query("SELECT COUNT(o) > 0 FROM OrdenDetalleEntity o WHERE o.producto.id = :id")
    boolean existeEnOrdenes(@Param("id") Integer id);

    @Query("SELECT COUNT(p) > 0 FROM ProductoEntity p WHERE p.categoriaEntity.id = :categoriaId")
    boolean existeProductosConCategoria(@Param("categoriaId") Integer categoriaId);
}
