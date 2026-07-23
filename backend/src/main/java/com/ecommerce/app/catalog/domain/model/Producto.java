package com.ecommerce.app.catalog.domain.model;

import java.math.BigDecimal;

public class Producto {
    private Integer id;
    private String nombre;
    private String sku;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String imagenNombre;
    private boolean activo = true;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(Integer id, String nombre, String sku, String descripcion, BigDecimal precio, int stock, String imagenNombre, boolean activo, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.sku = sku;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenNombre = imagenNombre;
        this.categoria = categoria;
        this.activo = activo;
    }

    public boolean isDisponible() {
        return activo && stock > 0;
    }

    public boolean tieneStockSuficiente(int cantidadRequerida) {
        return stock >= cantidadRequerida;
    }
    
    public void reducirStock(int cantidad) {
    	if (!tieneStockSuficiente(cantidad)) {
    		throw new IllegalArgumentException("Stock insuficiente. Disponible: " + stock + ", requerido: " + cantidad);
    	}
        this.stock -= cantidad;
    }
    
    public void incrementarStock(int cantidad) {
    	if (cantidad <= 0 ) {
    		throw new IllegalArgumentException("La cantidad debe ser positiva");
    	}
    	this.stock += cantidad;
    }

    public BigDecimal calcularValorInventario() {
        return precio.multiply(BigDecimal.valueOf(stock));
    }

    public boolean requiereReposicion(int stockMinimo) {
        return stock <= stockMinimo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagenNombre() {
        return imagenNombre;
    }

    public void setImagenNombre(String imagenNombre) {
        this.imagenNombre = imagenNombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return id != null && id.equals(producto.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
