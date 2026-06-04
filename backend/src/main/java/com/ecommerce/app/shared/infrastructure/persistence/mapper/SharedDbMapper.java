package com.ecommerce.app.shared.infrastructure.persistence.mapper;

import com.ecommerce.app.shared.domain.model.*;
import com.ecommerce.app.shared.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class SharedDbMapper {

    // --- ROL ---
    public Rol toDomain(RolEntity entity) {
        if (entity == null) return null;
        Rol domain = new Rol(entity.getId(), entity.getNombre());
        if (entity.getUsuarios() != null) {
            domain.setUsuarios(entity.getUsuarios().stream()
                .map(this::toDomainShallow)
                .collect(Collectors.toList()));
        }
        return domain;
    }

    public Rol toDomainShallow(RolEntity entity) {
        if (entity == null) return null;
        return new Rol(entity.getId(), entity.getNombre());
    }

    public RolEntity toEntity(Rol domain) {
        if (domain == null) return null;
        RolEntity entity = new RolEntity(domain.getId(), domain.getNombre());
        return entity;
    }

    // --- USUARIO ---
    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        return new Usuario(
            entity.getId(),
            entity.getNombre(),
            entity.getCorreo(),
            entity.getContrasena(),
            entity.isActivo(),
            toDomainShallow(entity.getRol()),
            entity.getDobleFactorActivo()
        );
    }

    public Usuario toDomainShallow(UsuarioEntity entity) {
        if (entity == null) return null;
        return new Usuario(
            entity.getId(),
            entity.getNombre(),
            entity.getCorreo(),
            entity.getContrasena(),
            entity.isActivo(),
            null,
            entity.getDobleFactorActivo()
        );
    }

    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) return null;
        return new UsuarioEntity(
            domain.getId(),
            domain.getCorreo(),
            domain.getNombre(),
            domain.getContrasena(),
            domain.isActivo(),
            toEntity(domain.getRol()),
            domain.getDobleFactorActivo()
        );
    }

    // --- AREA ---
    public Area toDomain(AreaEntity entity) {
        if (entity == null) return null;
        Area domain = new Area(entity.getId(), entity.getNombre());
        if (entity.getEmpleados() != null) {
            domain.setEmpleados(entity.getEmpleados().stream()
                .map(this::toDomainShallow)
                .collect(Collectors.toList()));
        }
        return domain;
    }

    public Area toDomainShallow(AreaEntity entity) {
        if (entity == null) return null;
        return new Area(entity.getId(), entity.getNombre());
    }

    public AreaEntity toEntity(Area domain) {
        if (domain == null) return null;
        return new AreaEntity(domain.getId(), domain.getNombre());
    }

    // --- CARGO ---
    public Cargo toDomain(CargoEntity entity) {
        if (entity == null) return null;
        Cargo domain = new Cargo(entity.getId(), entity.getNombre());
        if (entity.getEmpleados() != null) {
            domain.setEmpleados(entity.getEmpleados().stream()
                .map(this::toDomainShallow)
                .collect(Collectors.toList()));
        }
        return domain;
    }

    public Cargo toDomainShallow(CargoEntity entity) {
        if (entity == null) return null;
        return new Cargo(entity.getId(), entity.getNombre());
    }

    public CargoEntity toEntity(Cargo domain) {
        if (domain == null) return null;
        return new CargoEntity(domain.getId(), domain.getNombre());
    }

    // --- EMPLEADO ---
    public Empleado toDomain(EmpleadoEntity entity) {
        if (entity == null) return null;
        return new Empleado(
            entity.getId(),
            toDomain(entity.getUsuario()),
            toDomainShallow(entity.getArea()),
            toDomainShallow(entity.getCargo())
        );
    }

    public Empleado toDomainShallow(EmpleadoEntity entity) {
        if (entity == null) return null;
        return new Empleado(
            entity.getId(),
            toDomainShallow(entity.getUsuario()),
            null,
            null
        );
    }

    public EmpleadoEntity toEntity(Empleado domain) {
        if (domain == null) return null;
        return new EmpleadoEntity(
            domain.getId(),
            toEntity(domain.getUsuario()),
            toEntity(domain.getArea()),
            toEntity(domain.getCargo())
        );
    }
}
