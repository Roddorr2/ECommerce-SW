package com.ecommerce.app.sales.infrastructure.persistence;

import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.sales.infrastructure.persistence.mapper.SalesDbMapper;
import com.ecommerce.app.sales.infrastructure.persistence.repository.ClienteJpaRepository;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClienteJpaAdapter implements ClientePort {
    private final ClienteJpaRepository clienteJpaRepository;
    private final SalesDbMapper salesDbMapper;
    private final SharedDbMapper sharedDbMapper;

    public ClienteJpaAdapter(ClienteJpaRepository clienteJpaRepository, SalesDbMapper salesDbMapper, SharedDbMapper sharedDbMapper) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.salesDbMapper = salesDbMapper;
        this.sharedDbMapper = sharedDbMapper;
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteJpaRepository.findById(id)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorUsuario(Usuario usuario) {
        return clienteJpaRepository.findByUsuario(sharedDbMapper.toEntity(usuario))
                .map(salesDbMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorUsuarioCorreo(String correo) {
        return clienteJpaRepository.findByUsuarioCorreo(correo)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteJpaRepository.findAll()
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity entity = salesDbMapper.toEntity(cliente);
        ClienteEntity guardado = clienteJpaRepository.save(entity);
        return salesDbMapper.toDomain(guardado);
    }

    @Override
    public void eliminar(Cliente cliente) {
        ClienteEntity entity = salesDbMapper.toEntity(cliente);
        clienteJpaRepository.delete(entity);
    }
}
