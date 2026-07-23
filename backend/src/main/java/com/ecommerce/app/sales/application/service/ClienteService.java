package com.ecommerce.app.sales.application.service;

import com.ecommerce.app.sales.application.dto.request.ActualizarClienteRequest;
import com.ecommerce.app.sales.application.dto.request.CrearClienteRequest;
import com.ecommerce.app.sales.application.dto.response.ClienteResponse;
import com.ecommerce.app.sales.application.mapper.ClienteMapper;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.UsuarioPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClientePort clientePort;
    private final UsuarioPort usuarioPort;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClientePort clientePort, UsuarioPort usuarioPort, ClienteMapper clienteMapper) {
        this.clientePort = clientePort;
        this.usuarioPort = usuarioPort;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteResponse> listarClientes() {
        return clientePort.listarTodos().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    public ClienteResponse obtenerPorId(int id) {
        Cliente cliente = clientePort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponse(cliente);
    }

    public ClienteResponse obtenerPorUsuarioId(int usuarioId) {
        Usuario usuario = usuarioPort.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));
        Cliente cliente = clientePort.buscarPorUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado para el usuario ID: " + usuarioId));
        return clienteMapper.toResponse(cliente);
    }

    @Transactional
    public ClienteResponse registrarCliente(CrearClienteRequest request) {
        Usuario usuario = usuarioPort.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.usuarioId()));

        clientePort.buscarPorUsuario(usuario).ifPresent(c -> {
            throw new IllegalArgumentException("El usuario ya está asignado a otro cliente.");
        });

        Cliente cliente = clienteMapper.toEntity(request, usuario);
        Cliente savedCliente = clientePort.guardar(cliente);
        return clienteMapper.toResponse(savedCliente);
    }

    @Transactional
    public ClienteResponse editarCliente(ActualizarClienteRequest request) {
        Cliente cliente = clientePort.buscarPorId(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + request.id()));

        clienteMapper.updateEntity(cliente, request);
        Cliente savedCliente = clientePort.guardar(cliente);
        return clienteMapper.toResponse(savedCliente);
    }

    @Transactional
    public ClienteResponse actualizarPerfil(Integer usuarioId, ActualizarClienteRequest request) {
        Usuario usuario = usuarioPort.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        Cliente cliente = clientePort.buscarPorUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado para el usuario autenticado."));

        if (!cliente.getId().equals(request.id())) {
            throw new IllegalArgumentException("No está autorizado a modificar este perfil.");
        }

        clienteMapper.updateEntity(cliente, request);
        Cliente savedCliente = clientePort.guardar(cliente);
        return clienteMapper.toResponse(savedCliente);
    }

    @Transactional
    public void eliminarCliente(int id) {
        Cliente cliente = clientePort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        clientePort.eliminar(cliente);
    }
}
