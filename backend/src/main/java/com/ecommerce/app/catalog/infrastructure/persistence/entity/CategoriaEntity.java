package com.ecommerce.app.catalog.infrastructure.persistence.entity;

import com.ecommerce.app.catalog.domain.model.Categoria;
import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class CategoriaEntity extends Categoria {

    public CategoriaEntity() {
        super();
    }

    public CategoriaEntity(Integer id, String nombre) {
        super(id, nombre);
    }

    public CategoriaEntity(Categoria categoria) {
        super(categoria.getId(), categoria.getNombre());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(nullable = false, unique = true)
    @Override
    public String getNombre() {
        return super.getNombre();
    }
}
