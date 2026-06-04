package com.ecommerce.app.auth.domain.port;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.shared.domain.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CodigoVerificacionPort {
    Optional<CodigoVerificacion> buscarPorUsuarioYCodigoYEstado(Usuario usuario, String codigo, EstadoCodigoVerificacionCodigo estado);
    long contarPorUsuarioYFechaGeneracionDespues(Usuario usuario, LocalDateTime desde);
    List<CodigoVerificacion> buscarPorUsuarioYEstado(Usuario usuario, EstadoCodigoVerificacionCodigo estado);
    List<CodigoVerificacion> buscarCodigosExpirados(EstadoCodigoVerificacionCodigo estado, LocalDateTime fecha);
    CodigoVerificacion guardar(CodigoVerificacion codigo);
    List<CodigoVerificacion> guardarTodos(List<CodigoVerificacion> codigos);
}
