package com.ecommerce.app.purchases.infrastructure.persistence.repository;

import com.ecommerce.app.purchases.infrastructure.persistence.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorJpaRepository extends JpaRepository<ProveedorEntity, Integer> {
    @Query(value = """
            SELECT p.*
            FROM proveedor p
            INNER JOIN tipo_proveedor tp ON p.tipo_proveedor_id = tp.id
            WHERE LOWER(tp.nombre) LIKE LOWER(CONCAT('%', :tipoNombre, '%'))
        """, nativeQuery = true)
    List<ProveedorEntity> buscarPorTipoProveedor(@Param("tipoNombre") String tipoNombre);
    Optional<ProveedorEntity> findByCorreo(String correo);
}
