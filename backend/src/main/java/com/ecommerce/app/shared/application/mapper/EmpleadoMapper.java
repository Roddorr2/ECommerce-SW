package com.ecommerce.app.shared.application.mapper;

import com.ecommerce.app.shared.application.dto.request.ActualizarEmpleadoRequest;
import org.springframework.stereotype.Component;

import com.ecommerce.app.shared.application.dto.request.CrearEmpleadoRequest;
import com.ecommerce.app.shared.application.dto.response.EmpleadoBasicoResponse;
import com.ecommerce.app.shared.application.dto.response.EmpleadoResponse;
import com.ecommerce.app.shared.domain.model.Area;
import com.ecommerce.app.shared.domain.model.Cargo;
import com.ecommerce.app.shared.domain.model.Empleado;
import com.ecommerce.app.shared.domain.model.Usuario;

@Component
public class EmpleadoMapper {

    public Empleado toEntity(CrearEmpleadoRequest request, Usuario usuario, Area area, Cargo cargo) {
        if (request == null) {
            return null;
        }

        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);
        empleado.setArea(area);
        empleado.setCargo(cargo);

        return empleado;
    }

    public void updateEntity(Empleado empleado, ActualizarEmpleadoRequest request, Usuario usuario, Area area, Cargo cargo) {
        if (empleado == null || request == null) {
            return;
        }

        empleado.setUsuario(usuario);
        empleado.setArea(area);
        empleado.setCargo(cargo);
    }

    public EmpleadoBasicoResponse toBasicoResponse(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoBasicoResponse(
                empleado.getId(),
                empleado.getUsuario().getNombre(),
                empleado.getUsuario().getCorreo(),
                empleado.getArea().getNombre(),
                empleado.getCargo().getNombre()
        );
    }

    public EmpleadoResponse toResponse(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getUsuario().getId(),
                empleado.getUsuario().getNombre(),
                empleado.getUsuario().getCorreo(),
                empleado.getArea().getNombre(),
                empleado.getCargo().getNombre()
        );
    }
}
