package com.ecommerce.app.catalog.infrastructure.persistence.entity;

import com.ecommerce.app.catalog.domain.model.Producto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class ProductoEntity extends Producto {

    public ProductoEntity() {
        super();
    }

    public ProductoEntity(Producto producto) {
        super(producto.getId(), producto.getNombre(), producto.getSku(), 
              producto.getDescripcion(), producto.getPrecio(), producto.getStock(), 
              producto.getImagenNombre(), producto.isActivo(), producto.getCategoria());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(nullable = false)
    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Column(nullable = false, unique = true)
    @Override
    public String getSku() {
        return super.getSku();
    }

    @Column(columnDefinition = "TEXT")
    @Override
    public String getDescripcion() {
        return super.getDescripcion();
    }

    @Column(nullable = false, precision = 7, scale = 2)
    @Override
    public BigDecimal getPrecio() {
        return super.getPrecio();
    }

    @Column(nullable = false)
    @Override
    public int getStock() {
        return super.getStock();
    }

    @Column(name = "imagen_nombre")
    @Override
    public String getImagenNombre() {
        return super.getImagenNombre();
    }

    @Column(nullable = false)
    @Override
    public boolean isActivo() {
        return super.isActivo();
    }

    @Transient
    @Override
    public com.ecommerce.app.catalog.domain.model.Categoria getCategoria() {
        return super.getCategoria();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false, foreignKey = @ForeignKey(name = "fk_producto_categoria"))
    public CategoriaEntity getCategoriaEntity() {
        if (super.getCategoria() == null) {
            return null;
        }
        if (super.getCategoria() instanceof CategoriaEntity) {
            return (CategoriaEntity) super.getCategoria();
        }
        return new CategoriaEntity(super.getCategoria().getId(), super.getCategoria().getNombre());
    }

    public void setCategoriaEntity(CategoriaEntity categoriaEntity) {
        super.setCategoria(categoriaEntity);
    }
}
