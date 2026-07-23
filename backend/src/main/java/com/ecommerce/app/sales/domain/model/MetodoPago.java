package com.ecommerce.app.sales.domain.model;

import java.util.ArrayList;
import java.util.List;

public class MetodoPago {

    private Integer id;
    private String nombre;
    private List<Orden> ordenes = new ArrayList<>();

    public MetodoPago() {}

    public MetodoPago(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }
}
