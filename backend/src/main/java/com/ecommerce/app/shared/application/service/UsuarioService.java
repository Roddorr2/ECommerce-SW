package com.ecommerce.app.shared.application.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.app.shared.application.dto.request.ActualizarUsuarioRequest;
import com.ecommerce.app.shared.application.dto.request.CrearUsuarioRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.app.shared.application.dto.response.UsuarioResponse;
import com.ecommerce.app.shared.application.mapper.UsuarioMapper;
import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.RolPort;
import com.ecommerce.app.shared.domain.port.UsuarioPort;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioPort usuarioPort;
    private final RolPort rolPort;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioPort usuarioPort, RolPort rolPort, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioPort = usuarioPort;
        this.rolPort = rolPort;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioPort.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    public UsuarioResponse obtenerPorId(int id) {
        Usuario usuario = usuarioPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID " + id));

        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse registrarUsuario(CrearUsuarioRequest request) {
        Optional<Usuario> usuarioExistente = usuarioPort.findByCorreo(request.correo());

        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + request.correo());
        }

        Rol rol = rolPort.findById(request.rolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido con ID: " + request.rolId()));

        String contrasenaEncriptada = passwordEncoder.encode(request.contrasena());
        Usuario usuario = usuarioMapper.toEntity(request, rol, contrasenaEncriptada);

        Usuario usuarioGuardado = usuarioPort.save(usuario);
        return usuarioMapper.toResponse(usuarioGuardado);
    }

    @Transactional
    public UsuarioResponse editarUsuario(ActualizarUsuarioRequest request) {
        Usuario usuario = usuarioPort.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.id()));

        usuarioPort.findByCorreo(request.correo()).ifPresent(u -> {
            if (!u.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro usuario con el correo: " + request.correo());
            }
        });

        Rol rol = rolPort.findById(request.rolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no válido con ID: " + request.rolId()));

        usuarioMapper.updateEntity(usuario, request, rol);
        Usuario usuarioActualizado = usuarioPort.save(usuario);

        return usuarioMapper.toResponse(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(int id) {
        Usuario usuario = usuarioPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioPort.delete(usuario);
    }
    
}
