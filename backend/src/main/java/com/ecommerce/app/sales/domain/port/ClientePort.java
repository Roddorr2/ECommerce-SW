package com.ecommerce.app.sales.domain.port;

import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.shared.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ClientePort {
    Optional<Cliente> buscarPorId(Integer id);
    Optional<Cliente> buscarPorUsuario(Usuario usuario);
    Optional<Cliente> buscarPorUsuarioCorreo(String correo);
    List<Cliente> listarTodos();
    Cliente guardar(Cliente cliente);
    void eliminar(Cliente cliente);
}
