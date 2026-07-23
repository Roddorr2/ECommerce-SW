package com.ecommerce.app.sales.application.mapper;

import com.ecommerce.app.sales.application.dto.request.ActualizarClienteRequest;
import com.ecommerce.app.sales.application.dto.request.CrearClienteRequest;
import com.ecommerce.app.sales.application.dto.response.ClienteBasicoResponse;
import com.ecommerce.app.sales.application.dto.response.ClienteResponse;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.shared.domain.model.Usuario;

import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteBasicoResponse toBasicoResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteBasicoResponse(
                cliente.getId(),
                cliente.getUsuario().getNombre(),
                cliente.getUsuario().getCorreo(),
                cliente.getTelefono()
        );
    }

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponse(
                cliente.getId(),
                cliente.getUsuario().getId(),
                cliente.getUsuario().getNombre(),
                cliente.getUsuario().getCorreo(),
                cliente.getTelefono(),
                cliente.getDireccion()
        );
    }

    public Cliente toEntity(CrearClienteRequest request, Usuario usuario) {
    	if (request == null) {
    		return null;
    	}
    	
    	Cliente cliente = new Cliente();
    	cliente.setUsuario(usuario);
    	cliente.setTelefono(request.telefono());
    	cliente.setDireccion(request.direccion());
    	
    	return cliente;
    }
    
    public void updateEntity(Cliente cliente, ActualizarClienteRequest request) {
    	if (cliente == null || request == null) {
    		return;
    	}
    	
    	cliente.setTelefono(request.telefono());
    	cliente.setDireccion(request.direccion());
    }
}
