package com.ecommerce.app.purchases.unit;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.purchases.application.dto.request.CambiarEstadoCompraRequest;
import com.ecommerce.app.purchases.application.dto.request.CompraItemRequest;
import com.ecommerce.app.purchases.application.dto.request.CrearCompraRequest;
import com.ecommerce.app.purchases.application.dto.response.CompraDetalleResponse;
import com.ecommerce.app.purchases.application.dto.response.CompraResponse;
import com.ecommerce.app.purchases.application.dto.response.CompraResumenResponse;
import com.ecommerce.app.purchases.application.dto.response.EstadoCompraResponse;
import com.ecommerce.app.purchases.application.dto.response.ProveedorBasicoResponse;
import com.ecommerce.app.purchases.application.mapper.CompraMapper;
import com.ecommerce.app.purchases.application.service.CompraService;
import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.port.CompraPort;
import com.ecommerce.app.purchases.domain.port.ProveedorPort;
import com.ecommerce.app.shared.application.dto.response.EmpleadoBasicoResponse;
import com.ecommerce.app.shared.domain.event.CompraRecibidaEvent;
import com.ecommerce.app.shared.domain.model.Empleado;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.EmpleadoPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompraService")
public class CompraServiceTest {

    @Mock
    private CompraPort compraPort;

    @Mock
    private EmpleadoPort empleadoPort;

    @Mock
    private ProveedorPort proveedorPort;

    @Mock
    private ProductoPort productoPort;

    @Mock
    private CompraMapper compraMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CompraService compraService;

    private Proveedor proveedor;
    private Usuario usuarioResponsable;
    private Empleado empleado;
    private Producto producto;
    private Compra compraPendiente;
    private Compra compraRecibida;
    private Compra compraCancelada;
    private CompraResponse compraResponse;
    private CompraResumenResponse compraResumenResponse;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor(1, "Proveedor Central", "123456789", "prov@central.com", "Av. Principal 123", null);
        usuarioResponsable = new Usuario();
        usuarioResponsable.setId(10);
        usuarioResponsable.setNombre("Gerente Compras");
        usuarioResponsable.setCorreo("compras@ecommerce.com");

        empleado = new Empleado(5, usuarioResponsable, null, null);

        producto = new Producto(
                100, "Memoria RAM 16GB", "RAM-16G",
                "Memoria DDR4", BigDecimal.valueOf(80.00),
                20, null, true, null
        );

        compraPendiente = new Compra(1, LocalDate.now(), EstadoCompraCodigo.PENDIENTE, proveedor, empleado);
        CompraDetalle detalle = new CompraDetalle(10, 5, BigDecimal.valueOf(80.00), producto, compraPendiente);
        compraPendiente.agregarDetalle(detalle);

        compraRecibida = new Compra(2, LocalDate.now(), EstadoCompraCodigo.RECIBIDA, proveedor, empleado);
        compraRecibida.agregarDetalle(new CompraDetalle(11, 5, BigDecimal.valueOf(80.00), producto, compraRecibida));

        compraCancelada = new Compra(3, LocalDate.now(), EstadoCompraCodigo.CANCELADA, proveedor, empleado);

        ProveedorBasicoResponse proveedorResponse = new ProveedorBasicoResponse(1, "Proveedor Central", "123456789", "prov@central.com");
        EmpleadoBasicoResponse empleadoResponse = new EmpleadoBasicoResponse(5, "Gerente Compras", "compras@ecommerce.com", "Compras", "Gerente");
        CompraDetalleResponse detalleResponse = new CompraDetalleResponse(10, null, 5, BigDecimal.valueOf(80.00), BigDecimal.valueOf(400.00));

        compraResponse = new CompraResponse(
                1, LocalDate.now(), BigDecimal.valueOf(400.00),
                new EstadoCompraResponse("PENDIENTE"),
                proveedorResponse, empleadoResponse, List.of(detalleResponse)
        );

        compraResumenResponse = new CompraResumenResponse(
                1, LocalDate.now(), "PENDIENTE", BigDecimal.valueOf(400.00),
                1, proveedorResponse, empleadoResponse
        );
    }

    @Nested
    @DisplayName("listarCompras")
    class ListarCompras {

        @Test
        @DisplayName("cuando existen compras, entonces retorna la lista de resúmenes mapeada")
        void cuandoExistenCompras_entoncesRetornaListaResumenes() {
            // Given
            when(compraPort.listarTodas()).thenReturn(List.of(compraPendiente));
            when(compraMapper.toResumenResponse(compraPendiente)).thenReturn(compraResumenResponse);

            // When
            List<CompraResumenResponse> resultado = compraService.listarCompras();

            // Then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).id()).isEqualTo(1);
            assertThat(resultado.get(0).estado()).isEqualTo("PENDIENTE");
            verify(compraPort).listarTodas();
            verify(compraMapper).toResumenResponse(compraPendiente);
        }

        @Test
        @DisplayName("cuando no existen compras, entonces retorna una lista vacía")
        void cuandoNoExistenCompras_entoncesRetornaListaVacia() {
            // Given
            when(compraPort.listarTodas()).thenReturn(List.of());

            // When
            List<CompraResumenResponse> resultado = compraService.listarCompras();

            // Then
            assertThat(resultado).isEmpty();
            verify(compraPort).listarTodas();
            verifyNoInteractions(compraMapper);
        }
    }

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("dado ID existente, cuando se obtiene por ID, entonces retorna la compra mapeada")
        void dadoIdExistente_cuandoObtenerPorId_entoncesRetornaCompraResponse() {
            // Given
            when(compraPort.buscarPorId(1)).thenReturn(Optional.of(compraPendiente));
            when(compraMapper.toResponse(compraPendiente)).thenReturn(compraResponse);

            // When
            CompraResponse resultado = compraService.obtenerPorId(1);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.id()).isEqualTo(1);
            assertThat(resultado.total()).isEqualTo(BigDecimal.valueOf(400.00));
            verify(compraPort).buscarPorId(1);
            verify(compraMapper).toResponse(compraPendiente);
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se obtiene por ID, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoObtenerPorId_entoncesLanzaEntityNotFoundException() {
            // Given
            when(compraPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.obtenerPorId(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Compra no encontrada con ID: 99");

            verify(compraPort).buscarPorId(99);
            verifyNoInteractions(compraMapper);
        }
    }

    @Nested
    @DisplayName("crearCompra")
    class CrearCompra {

        @Test
        @DisplayName("dado request válido, cuando se crea la compra, entonces la guarda con estado PENDIENTE y la retorna")
        void dadoRequestValido_cuandoCrearCompra_entoncesGuardaYRetornaCompra() {
            // Given
            CompraItemRequest itemRequest = new CompraItemRequest(100, 5, BigDecimal.valueOf(80.00));
            CrearCompraRequest request = new CrearCompraRequest(1, 5, List.of(itemRequest));

            when(proveedorPort.buscarPorId(1)).thenReturn(Optional.of(proveedor));
            when(empleadoPort.findById(5)).thenReturn(Optional.of(empleado));
            when(productoPort.buscarPorId(100)).thenReturn(Optional.of(producto));

            ArgumentCaptor<Compra> captor = ArgumentCaptor.forClass(Compra.class);
            when(compraPort.guardar(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));
            when(compraMapper.toResponse(any(Compra.class))).thenReturn(compraResponse);

            // When
            CompraResponse resultado = compraService.crearCompra(request);

            // Then
            assertThat(resultado).isNotNull();
            verify(compraPort).guardar(captor.capture());

            Compra guardada = captor.getValue();
            assertThat(guardada.getProveedor()).isEqualTo(proveedor);
            assertThat(guardada.getEmpleado()).isEqualTo(empleado);
            assertThat(guardada.getEstadoCompra()).isEqualTo(EstadoCompraCodigo.PENDIENTE);
            assertThat(guardada.getDetalles()).hasSize(1);
            assertThat(guardada.getDetalles().get(0).getProducto()).isEqualTo(producto);
            assertThat(guardada.getDetalles().get(0).getCantidad()).isEqualTo(5);
            assertThat(guardada.getDetalles().get(0).getPrecioUnitario()).isEqualTo(BigDecimal.valueOf(80.00));

            verify(proveedorPort).buscarPorId(1);
            verify(empleadoPort).findById(5);
            verify(productoPort).buscarPorId(100);
        }

        @Test
        @DisplayName("dado proveedor inexistente, cuando se crea la compra, entonces lanza EntityNotFoundException")
        void dadoProveedorInexistente_cuandoCrearCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            CrearCompraRequest request = new CrearCompraRequest(99, 5, List.of());
            when(proveedorPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.crearCompra(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Proveedor no encontrado con ID: 99");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado empleado inexistente, cuando se crea la compra, entonces lanza EntityNotFoundException")
        void dadoEmpleadoInexistente_cuandoCrearCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            CrearCompraRequest request = new CrearCompraRequest(1, 99, List.of());
            when(proveedorPort.buscarPorId(1)).thenReturn(Optional.of(proveedor));
            when(empleadoPort.findById(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.crearCompra(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Empleado no encontrado con ID: 99");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado producto inexistente, cuando se crea la compra, entonces lanza EntityNotFoundException")
        void dadoProductoInexistente_cuandoCrearCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            CompraItemRequest itemRequest = new CompraItemRequest(999, 5, BigDecimal.valueOf(80.00));
            CrearCompraRequest request = new CrearCompraRequest(1, 5, List.of(itemRequest));

            when(proveedorPort.buscarPorId(1)).thenReturn(Optional.of(proveedor));
            when(empleadoPort.findById(5)).thenReturn(Optional.of(empleado));
            when(productoPort.buscarPorId(999)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.crearCompra(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Producto no encontrado con ID: 999");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado request sin items, cuando se crea la compra, entonces lanza IllegalStateException")
        void dadoRequestSinItems_cuandoCrearCompra_entoncesLanzaIllegalStateException() {
            // Given
            CrearCompraRequest request = new CrearCompraRequest(1, 5, List.of());

            when(proveedorPort.buscarPorId(1)).thenReturn(Optional.of(proveedor));
            when(empleadoPort.findById(5)).thenReturn(Optional.of(empleado));

            // When / Then
            assertThatThrownBy(() -> compraService.crearCompra(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("La compra debe tener al menos un producto");

            verify(compraPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("recibirCompra")
    class RecibirCompra {

        @Test
        @DisplayName("dado compra PENDIENTE válida, cuando se recibe, entonces publica CompraRecibidaEvent y cambia estado a RECIBIDA")
        void dadoCompraPendiente_cuandoRecibirCompra_entoncesPublicaEventYCambiaEstado() {
            // Given
            producto.setStock(20); // stock inicial
            when(compraPort.buscarPorId(1)).thenReturn(Optional.of(compraPendiente));
            when(compraPort.guardar(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));
            when(compraMapper.toResponse(any(Compra.class))).thenReturn(compraResponse);

            // When
            CompraResponse resultado = compraService.recibirCompra(1);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(compraPendiente.getEstadoCompra()).isEqualTo(EstadoCompraCodigo.RECIBIDA);

            verify(eventPublisher).publishEvent(any(CompraRecibidaEvent.class));
            verify(compraPort).guardar(compraPendiente);
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se recibe, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoRecibirCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            when(compraPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.recibirCompra(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Compra no encontrada con ID: 99");

            verifyNoInteractions(eventPublisher);
            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado compra que ya fue RECIBIDA, cuando se recibe, entonces lanza IllegalStateException")
        void dadoCompraYaRecibida_cuandoRecibirCompra_entoncesLanzaIllegalStateException() {
            // Given
            when(compraPort.buscarPorId(2)).thenReturn(Optional.of(compraRecibida));

            // When / Then
            assertThatThrownBy(() -> compraService.recibirCompra(2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Esta compra no puede recibirse. Estado actual: RECIBIDA");

            verifyNoInteractions(eventPublisher);
            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado compra CANCELADA, cuando se recibe, entonces lanza IllegalStateException")
        void dadoCompraCancelada_cuandoRecibirCompra_entoncesLanzaIllegalStateException() {
            // Given
            when(compraPort.buscarPorId(3)).thenReturn(Optional.of(compraCancelada));

            // When / Then
            assertThatThrownBy(() -> compraService.recibirCompra(3))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Esta compra no puede recibirse. Estado actual: CANCELADA");

            verifyNoInteractions(eventPublisher);
            verify(compraPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("cancelarCompra")
    class CancelarCompra {

        @Test
        @DisplayName("dado compra PENDIENTE, cuando se cancela, entonces cambia estado a CANCELADA")
        void dadoCompraPendiente_cuandoCancelarCompra_entoncesCambiaEstadoACancelada() {
            // Given
            when(compraPort.buscarPorId(1)).thenReturn(Optional.of(compraPendiente));
            when(compraPort.guardar(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));
            when(compraMapper.toResponse(any(Compra.class))).thenReturn(compraResponse);

            // When
            CompraResponse resultado = compraService.cancelarCompra(1, "Ya no se requiere");

            // Then
            assertThat(resultado).isNotNull();
            assertThat(compraPendiente.getEstadoCompra()).isEqualTo(EstadoCompraCodigo.CANCELADA);
            verify(compraPort).guardar(compraPendiente);
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se cancela, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoCancelarCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            when(compraPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.cancelarCompra(99, "Motivo"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Compra no encontrada");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado compra RECIBIDA, cuando se cancela, entonces lanza IllegalStateException")
        void dadoCompraRecibida_cuandoCancelarCompra_entoncesLanzaIllegalStateException() {
            // Given
            when(compraPort.buscarPorId(2)).thenReturn(Optional.of(compraRecibida));

            // When / Then
            assertThatThrownBy(() -> compraService.cancelarCompra(2, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Esta compra no puede cancelarse. Estado actual: RECIBIDA");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado compra ya CANCELADA, cuando se cancela, entonces lanza IllegalStateException")
        void dadoCompraYaCancelada_cuandoCancelarCompra_entoncesLanzaIllegalStateException() {
            // Given
            when(compraPort.buscarPorId(3)).thenReturn(Optional.of(compraCancelada));

            // When / Then
            assertThatThrownBy(() -> compraService.cancelarCompra(3, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Esta compra no puede cancelarse. Estado actual: CANCELADA");

            verify(compraPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("cambiarEstado")
    class CambiarEstado {

        @Test
        @DisplayName("dado compra mutable y estado válido, cuando se cambia estado, entonces actualiza y retorna la compra")
        void dadoCompraMutableYEstadoValido_cuandoCambiarEstado_entoncesActualizaYRetorna() {
            // Given
            CambiarEstadoCompraRequest request = new CambiarEstadoCompraRequest(EstadoCompraCodigo.CANCELADA);
            when(compraPort.buscarPorId(1)).thenReturn(Optional.of(compraPendiente));
            when(compraPort.guardar(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));
            when(compraMapper.toResponse(any(Compra.class))).thenReturn(compraResponse);

            // When
            CompraResponse resultado = compraService.cambiarEstado(1, request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(compraPendiente.getEstadoCompra()).isEqualTo(EstadoCompraCodigo.CANCELADA);
            verify(compraPort).guardar(compraPendiente);
        }

        @Test
        @DisplayName("dado compra finalizada, cuando se cambia estado, entonces lanza IllegalStateException")
        void dadoCompraFinalizada_cuandoCambiarEstado_entoncesLanzaIllegalStateException() {
            // Given
            CambiarEstadoCompraRequest request = new CambiarEstadoCompraRequest(EstadoCompraCodigo.PENDIENTE);
            when(compraPort.buscarPorId(2)).thenReturn(Optional.of(compraRecibida));

            // When / Then
            assertThatThrownBy(() -> compraService.cambiarEstado(2, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede modificar una compra finalizada.");

            verify(compraPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se cambia estado, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoCambiarEstado_entoncesLanzaEntityNotFoundException() {
            // Given
            CambiarEstadoCompraRequest request = new CambiarEstadoCompraRequest(EstadoCompraCodigo.RECIBIDA);
            when(compraPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.cambiarEstado(99, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Compra no encontrada");

            verify(compraPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("obtenerComprasPorProveedor")
    class ObtenerComprasPorProveedor {

        @Test
        @DisplayName("dado proveedor existente, cuando se buscan sus compras, entonces retorna la lista mapeada ordenada")
        void dadoProveedorExistente_cuandoObtenerComprasPorProveedor_entoncesRetornaLista() {
            // Given
            when(proveedorPort.buscarPorId(1)).thenReturn(Optional.of(proveedor));
            when(compraPort.buscarPorProveedorOrdenado(proveedor)).thenReturn(List.of(compraPendiente));
            when(compraMapper.toResumenResponse(compraPendiente)).thenReturn(compraResumenResponse);

            // When
            List<CompraResumenResponse> resultado = compraService.obtenerComprasPorProveedor(1);

            // Then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).id()).isEqualTo(1);
            verify(proveedorPort).buscarPorId(1);
            verify(compraPort).buscarPorProveedorOrdenado(proveedor);
            verify(compraMapper).toResumenResponse(compraPendiente);
        }

        @Test
        @DisplayName("dado proveedor inexistente, cuando se buscan sus compras, entonces lanza EntityNotFoundException")
        void dadoProveedorInexistente_cuandoObtenerComprasPorProveedor_entoncesLanzaEntityNotFoundException() {
            // Given
            when(proveedorPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.obtenerComprasPorProveedor(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Proveedor no encontrado");

            verify(compraPort, never()).buscarPorProveedorOrdenado(any());
        }
    }

    @Nested
    @DisplayName("obtenerComprasPorEstado")
    class ObtenerComprasPorEstado {

        @Test
        @DisplayName("dado un estado, cuando se listan por estado, entonces retorna la lista mapeada ordenada")
        void dadoEstado_cuandoObtenerComprasPorEstado_entoncesRetornaLista() {
            // Given
            when(compraPort.buscarPorEstadoOrdenado(EstadoCompraCodigo.PENDIENTE)).thenReturn(List.of(compraPendiente));
            when(compraMapper.toResumenResponse(compraPendiente)).thenReturn(compraResumenResponse);

            // When
            List<CompraResumenResponse> resultado = compraService.obtenerComprasPorEstado(EstadoCompraCodigo.PENDIENTE);

            // Then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).estado()).isEqualTo("PENDIENTE");
            verify(compraPort).buscarPorEstadoOrdenado(EstadoCompraCodigo.PENDIENTE);
            verify(compraMapper).toResumenResponse(compraPendiente);
        }
    }

    @Nested
    @DisplayName("eliminarCompra")
    class EliminarCompra {

        @Test
        @DisplayName("dado compra no recibida, cuando se elimina, entonces elimina la compra del repositorio")
        void dadoCompraNoRecibida_cuandoEliminarCompra_entoncesElimina() {
            // Given
            when(compraPort.buscarPorId(1)).thenReturn(Optional.of(compraPendiente));

            // When
            compraService.eliminarCompra(1);

            // Then
            verify(compraPort).eliminar(compraPendiente);
        }

        @Test
        @DisplayName("dado compra RECIBIDA, cuando se elimina, entonces lanza IllegalStateException")
        void dadoCompraRecibida_cuandoEliminarCompra_entoncesLanzaIllegalStateException() {
            // Given
            when(compraPort.buscarPorId(2)).thenReturn(Optional.of(compraRecibida));

            // When / Then
            assertThatThrownBy(() -> compraService.eliminarCompra(2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede eliminar una compra que ya fue recibida (stock ya incrementado)");

            verify(compraPort, never()).eliminar(any());
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se elimina, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoEliminarCompra_entoncesLanzaEntityNotFoundException() {
            // Given
            when(compraPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> compraService.eliminarCompra(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Compra no encontrada con ID 99");

            verify(compraPort, never()).eliminar(any());
        }
    }
}
