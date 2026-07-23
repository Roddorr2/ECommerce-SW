package com.ecommerce.app.purchases.infrastructure.persistence.entity;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "compra_detalle")
public class CompraDetalleEntity extends CompraDetalle {

    public CompraDetalleEntity() {
        super();
    }

    public CompraDetalleEntity(CompraDetalle detalle) {
        super(detalle.getId(), detalle.getCantidad(), detalle.getPrecioUnitario(),
              detalle.getProducto(), detalle.getCompra());
    }

    public CompraDetalleEntity(Integer id, int cantidad, BigDecimal precioUnitario, Producto producto, Compra compra) {
        super(id, cantidad, precioUnitario, producto, compra);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(nullable = false)
    @Override
    public int getCantidad() {
        return super.getCantidad();
    }

    @Column(name = "precio_unitario", nullable = false, precision = 7, scale = 2)
    @Override
    public BigDecimal getPrecioUnitario() {
        return super.getPrecioUnitario();
    }

    @Transient
    @Override
    public Producto getProducto() {
        return super.getProducto();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_detalle_producto"))
    public ProductoEntity getProductoEntity() {
        if (super.getProducto() == null) {
            return null;
        }
        if (super.getProducto() instanceof ProductoEntity) {
            return (ProductoEntity) super.getProducto();
        }
        return new ProductoEntity(super.getProducto());
    }

    public void setProductoEntity(ProductoEntity productoEntity) {
        super.setProducto(productoEntity);
    }

    @Transient
    @Override
    public Compra getCompra() {
        return super.getCompra();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_detalle_compra"))
    public CompraEntity getCompraEntity() {
        if (super.getCompra() == null) {
            return null;
        }
        if (super.getCompra() instanceof CompraEntity) {
            return (CompraEntity) super.getCompra();
        }
        return new CompraEntity(super.getCompra());
    }

    public void setCompraEntity(CompraEntity compraEntity) {
        super.setCompra(compraEntity);
    }
}
