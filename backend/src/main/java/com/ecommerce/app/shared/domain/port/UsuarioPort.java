package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioPort {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByCorreo(String correo);
    Usuario save(Usuario usuario);
    void delete(Usuario usuario);
}
