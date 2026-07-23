package com.ecommerce.app.shared.application.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.app.shared.application.dto.request.ActualizarUsuarioRequest;
import com.ecommerce.app.shared.application.dto.request.CrearUsuarioRequest;
import com.ecommerce.app.shared.application.dto.response.UsuarioResponse;
import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.model.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(CrearUsuarioRequest request, Rol rol, String contrasenaEncriptada) {
        if (request == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setContrasena(contrasenaEncriptada);
        usuario.setActivo(request.activo());
        usuario.setRol(rol);
        usuario.setDobleFactorActivo(false);
        return usuario;
    }

    public void updateEntity(Usuario usuario, ActualizarUsuarioRequest request, Rol rol) {
        if (usuario == null || request == null) {
            return;
        }

        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setActivo(request.activo());
        usuario.setRol(rol);
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.isActivo(),
                usuario.getRol().getNombre(),
                usuario.getDobleFactorActivo()
        );
    }
}