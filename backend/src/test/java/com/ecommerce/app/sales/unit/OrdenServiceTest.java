package com.ecommerce.app.sales.unit;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.application.dto.response.ProductoBasicoResponse;
import com.ecommerce.app.sales.application.dto.request.CambiarEstadoOrdenRequest;
import com.ecommerce.app.sales.application.dto.request.CrearOrdenDesdeCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.*;
import com.ecommerce.app.sales.application.mapper.OrdenMapper;
import com.ecommerce.app.sales.application.service.OrdenService;
import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.domain.model.*;
import com.ecommerce.app.sales.domain.port.OrdenPort;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.sales.domain.port.MetodoPagoPort;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import com.ecommerce.app.shared.domain.event.OrdenCanceladaEvent;
import com.ecommerce.app.shared.domain.event.OrdenCreadaEvent;
import com.ecommerce.app.shared.domain.model.Usuario;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrdenService")
public class OrdenServiceTest {

    @Mock
    private OrdenPort ordenPort;

    @Mock
    private ClientePort clientePort;

    @Mock
    private MetodoPagoPort metodoPagoPort;

    @Mock
    private CarritoPort carritoPort;

    @Mock
    private OrdenMapper ordenMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrdenService ordenService;

    private Cliente cliente;
    private Usuario usuario;
    private MetodoPago metodoPago;
    private Producto producto;
    private Carrito carrito;
    private CarritoItem carritoItem;
    private Orden ordenPendiente;
    private OrdenResponse ordenResponse;
    private OrdenResumenResponse ordenResumenResponse;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Carlos Gómez");
        usuario.setCorreo("carlos@ecommerce.com");

        cliente = new Cliente(1, usuario, "987654321", "Calle Mayor 456");

        metodoPago = new MetodoPago(1, "Tarjeta de Crédito");

        producto = new Producto(
                10, "Laptop Asus", "LAP-ASU",
                "Laptop de última generación", BigDecimal.valueOf(120.00),
                5, null, true, null
        );

        carrito = new Carrito(50, cliente, LocalDateTime.now(), LocalDateTime.now(), EstadoCarritoCodigo.ACTIVO);

        carritoItem = new CarritoItem(20, carrito, producto, 2, BigDecimal.valueOf(120.00), LocalDateTime.now());
        carrito.setItems(List.of(carritoItem));

        ordenPendiente = new Orden(100, LocalDateTime.now(), "Calle Mayor 456", EstadoOrdenCodigo.PENDIENTE, cliente, metodoPago);
        OrdenDetalle detalle = new OrdenDetalle(50, 2, BigDecimal.valueOf(120.00), ordenPendiente, producto);
        ordenPendiente.agregarDetalle(detalle);

        new Orden(101, LocalDateTime.now(), "Calle Mayor 456", EstadoOrdenCodigo.CANCELADO, cliente, metodoPago);

        ClienteBasicoResponse clienteResp = new ClienteBasicoResponse(1, "Carlos Gómez", "carlos@ecommerce.com", "987654321");
        MetodoPagoResponse metodoResp = new MetodoPagoResponse(1, "Tarjeta de Crédito");
        ProductoBasicoResponse prodResp = new ProductoBasicoResponse(10, "Laptop Asus", "LAP-ASU", BigDecimal.valueOf(120.00), 5, null);
        OrdenDetalleResponse detResp = new OrdenDetalleResponse(10, prodResp, 2, BigDecimal.valueOf(120.00), BigDecimal.valueOf(240.00));

        ordenResponse = new OrdenResponse(
                100, LocalDateTime.now(), "Calle Mayor 456", BigDecimal.valueOf(240.00),
                new EstadoOrdenResponse("PENDIENTE"), clienteResp, metodoResp, List.of(detResp)
        );

        ordenResumenResponse = new OrdenResumenResponse(
                100, LocalDateTime.now(), "PENDIENTE", BigDecimal.valueOf(240.00), 2, clienteResp
        );
    }

    @Nested
    @DisplayName("crearOrdenDesdeCarrito")
    class CrearOrdenDesdeCarrito {

        @Test
        @DisplayName("dado cliente y carrito válidos, cuando se crea la orden, entonces crea y guarda la orden correctamente")
        void dadoClienteYCarritoValidos_cuandoCrearOrdenDesdeCarrito_entoncesCreaOrdenExitosamente() {
            // Given
            CrearOrdenDesdeCarritoRequest request = new CrearOrdenDesdeCarritoRequest(1, "Calle Mayor 456");
            producto.setStock(15);

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carrito));
            when(metodoPagoPort.buscarPorId(1)).thenReturn(Optional.of(metodoPago));

            ArgumentCaptor<Orden> captorOrden = ArgumentCaptor.forClass(Orden.class);
            when(ordenPort.guardar(any(Orden.class))).thenAnswer(inv -> {
                Orden o = inv.getArgument(0);
                o.setId(100); // asigna ID al guardar
                return o;
            });
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));
            when(ordenMapper.toResponse(any(Orden.class))).thenReturn(ordenResponse);

            // When
            OrdenResponse resultado = ordenService.crearOrdenDesdeCarrito(1, request);

            // Then
            assertThat(resultado).isNotNull();
            verify(ordenPort).guardar(captorOrden.capture());

            Orden guardada = captorOrden.getValue();
            assertThat(guardada.getCliente()).isEqualTo(cliente);
            assertThat(guardada.getEstadoOrden()).isEqualTo(EstadoOrdenCodigo.PENDIENTE);
            assertThat(guardada.getMetodoPago()).isEqualTo(metodoPago);
            assertThat(guardada.getDireccionEnvio()).isEqualTo("Calle Mayor 456");
            assertThat(guardada.getDetalles()).hasSize(1);
            assertThat(guardada.getDetalles().get(0).getProducto()).isEqualTo(producto);
            assertThat(guardada.getDetalles().get(0).getCantidad()).isEqualTo(2);

            // Verificar publicación de evento
            verify(eventPublisher).publishEvent(any(OrdenCreadaEvent.class));

            // Verificar conversión de carrito
            assertThat(carrito.getEstadoCarrito()).isEqualTo(EstadoCarritoCodigo.CONVERTIDO);
            verify(carritoPort).guardar(carrito);
        }

        @Test
        @DisplayName("dado stock insuficiente en un ítem del carrito, cuando se crea la orden, entonces lanza IllegalStateException")
        void dadoStockInsuficiente_cuandoCrearOrdenDesdeCarrito_entoncesLanzaIllegalStateException() {
            // Given
            CrearOrdenDesdeCarritoRequest request = new CrearOrdenDesdeCarritoRequest(1, "Calle Mayor 456");
            producto.setStock(1); // stock es 1, pero carrito requiere 2

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carrito));

            // When / Then
            assertThatThrownBy(() -> ordenService.crearOrdenDesdeCarrito(1, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Stock insuficiente para");

            verify(ordenPort, never()).guardar(any());
            verifyNoInteractions(eventPublisher);
            verify(carritoPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cliente inexistente, cuando se crea la orden, entonces lanza EntityNotFoundException")
        void dadoClienteInexistente_cuandoCrearOrdenDesdeCarrito_entoncesLanzaEntityNotFoundException() {
            // Given
            CrearOrdenDesdeCarritoRequest request = new CrearOrdenDesdeCarritoRequest(1, "Calle Mayor 456");
            when(clientePort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> ordenService.crearOrdenDesdeCarrito(99, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Cliente no encontrado.");

            verify(ordenPort, never()).guardar(any());
            verifyNoInteractions(eventPublisher);
            verify(carritoPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("cambiarEstado")
    class CambiarEstado {

        @Test
        @DisplayName("dado orden existente y estado válido, cuando se cambia estado, entonces actualiza la orden")
        void dadoOrdenExistenteYEstadoValido_cuandoCambiarEstado_entoncesActualizaYRetornaOrden() {
            // Given
            CambiarEstadoOrdenRequest request = new CambiarEstadoOrdenRequest(EstadoOrdenCodigo.PAGADO);
            when(ordenPort.buscarPorId(100)).thenReturn(Optional.of(ordenPendiente));
            when(ordenPort.guardar(any(Orden.class))).thenAnswer(inv -> inv.getArgument(0));
            when(ordenMapper.toResponse(any(Orden.class))).thenReturn(ordenResponse);

            // When
            OrdenResponse resultado = ordenService.cambiarEstado(100, request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(ordenPendiente.getEstadoOrden()).isEqualTo(EstadoOrdenCodigo.PAGADO);
            verify(ordenPort).guardar(ordenPendiente);
        }

        @Test
        @DisplayName("dado orden con transición inválida, cuando se cambia estado, entonces lanza IllegalStateException")
        void dadoTransicionInvalida_cuandoCambiarEstado_entoncesLanzaIllegalStateException() {
            // Given
            CambiarEstadoOrdenRequest request = new CambiarEstadoOrdenRequest(EstadoOrdenCodigo.PENDIENTE);
            ordenPendiente.setEstadoOrden(EstadoOrdenCodigo.CANCELADO); // no puede transicionar a PENDIENTE
            when(ordenPort.buscarPorId(100)).thenReturn(Optional.of(ordenPendiente));

            // When / Then
            assertThatThrownBy(() -> ordenService.cambiarEstado(100, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede modificar una orden finalizada.");

            verify(ordenPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se cambia estado, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoCambiarEstado_entoncesLanzaEntityNotFoundException() {
            // Given
            CambiarEstadoOrdenRequest request = new CambiarEstadoOrdenRequest(EstadoOrdenCodigo.PAGADO);
            when(ordenPort.buscarPorId(999)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> ordenService.cambiarEstado(999, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Orden no encontrada");

            verify(ordenPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("cancelarOrden")
    class CancelarOrden {

        @Test
        @DisplayName("dado orden cancelable, cuando se cancela, entonces publica evento y transiciona a PENDIENTE")
        void dadoOrdenCancelable_cuandoCancelarOrden_entoncesPublicaEventYEstablecePendiente() {
            // Given
            producto.setStock(15);
            when(ordenPort.buscarPorId(100)).thenReturn(Optional.of(ordenPendiente));
            when(ordenPort.guardar(any(Orden.class))).thenAnswer(inv -> inv.getArgument(0));
            when(ordenMapper.toResponse(any(Orden.class))).thenReturn(ordenResponse);

            // When
            OrdenResponse resultado = ordenService.cancelarOrden(100, "Cliente desistió");

            // Then
            assertThat(resultado).isNotNull();
            assertThat(ordenPendiente.getEstadoOrden()).isEqualTo(EstadoOrdenCodigo.PENDIENTE);

            verify(eventPublisher).publishEvent(any(OrdenCanceladaEvent.class));
            verify(ordenPort).guardar(ordenPendiente);
        }

        @Test
        @DisplayName("dado orden que no puede cancelarse, cuando se cancela, entonces lanza IllegalStateException")
        void dadoOrdenNoCancelable_cuandoCancelarOrden_entoncesLanzaIllegalStateException() {
            // Given
            ordenPendiente.setEstadoOrden(EstadoOrdenCodigo.ENVIADO); // no es PENDIENTE ni PAGADO, por ende no es cancelable
            when(ordenPort.buscarPorId(100)).thenReturn(Optional.of(ordenPendiente));

            // When / Then
            assertThatThrownBy(() -> ordenService.cancelarOrden(100, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Esta orden no puede cancelarse.");

            verifyNoInteractions(eventPublisher);
            verify(ordenPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se cancela, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoCancelarOrden_entoncesLanzaEntityNotFoundException() {
            // Given
            when(ordenPort.buscarPorId(999)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> ordenService.cancelarOrden(999, "Motivo"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Orden no encontrada.");

            verifyNoInteractions(eventPublisher);
            verify(ordenPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("listarOrdenes")
    class ListarOrdenes {

        @Test
        @DisplayName("cuando existen ordenes, entonces retorna la lista de resúmenes")
        void cuandoExistenOrdenes_entoncesRetornaListaResumenes() {
            // Given
            when(ordenPort.listarTodas()).thenReturn(List.of(ordenPendiente));
            when(ordenMapper.toResumenResponse(ordenPendiente)).thenReturn(ordenResumenResponse);

            // When
            List<OrdenResumenResponse> resultado = ordenService.listarOrdenes();

            // Then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).id()).isEqualTo(100);
            verify(ordenPort).listarTodas();
            verify(ordenMapper).toResumenResponse(ordenPendiente);
        }
    }

    @Nested
    @DisplayName("obtenerOrdenesPorCliente")
    class ObtenerOrdenesPorCliente {

        @Test
        @DisplayName("dado cliente existente, cuando se listan sus órdenes, entonces retorna la lista de resúmenes ordenada")
        void dadoClienteExistente_cuandoObtenerOrdenesPorCliente_entoncesRetornaLista() {
            // Given
            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(ordenPort.buscarPorClienteOrdenado(cliente)).thenReturn(List.of(ordenPendiente));
            when(ordenMapper.toResumenResponse(ordenPendiente)).thenReturn(ordenResumenResponse);

            // When
            List<OrdenResumenResponse> resultado = ordenService.obtenerOrdenesPorCliente(1);

            // Then
            assertThat(resultado).hasSize(1);
            verify(clientePort).buscarPorId(1);
            verify(ordenPort).buscarPorClienteOrdenado(cliente);
        }
    }

    @Nested
    @DisplayName("obtenerOrdenesPorEstado")
    class ObtenerOrdenesPorEstado {

        @Test
        @DisplayName("dado estado, cuando se listan órdenes por estado, entonces retorna la lista de resúmenes")
        void dadoEstado_cuandoObtenerOrdenesPorEstado_entoncesRetornaLista() {
            // Given
            when(ordenPort.buscarPorEstadoOrdenado(EstadoOrdenCodigo.PENDIENTE)).thenReturn(List.of(ordenPendiente));
            when(ordenMapper.toResumenResponse(ordenPendiente)).thenReturn(ordenResumenResponse);

            // When
            List<OrdenResumenResponse> resultado = ordenService.obtenerOrdenesPorEstado(EstadoOrdenCodigo.PENDIENTE);

            // Then
            assertThat(resultado).hasSize(1);
            verify(ordenPort).buscarPorEstadoOrdenado(EstadoOrdenCodigo.PENDIENTE);
        }
    }

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("dado ID existente, cuando se obtiene por ID, entonces retorna la orden mapeada")
        void dadoIdExistente_cuandoObtenerPorId_entoncesRetornaOrdenResponse() {
            // Given
            when(ordenPort.buscarPorId(100)).thenReturn(Optional.of(ordenPendiente));
            when(ordenMapper.toResponse(ordenPendiente)).thenReturn(ordenResponse);

            // When
            OrdenResponse resultado = ordenService.obtenerPorId(100);

            // Then
            assertThat(resultado).isNotNull();
            verify(ordenPort).buscarPorId(100);
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se obtiene por ID, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoObtenerPorId_entoncesLanzaEntityNotFoundException() {
            // Given
            when(ordenPort.buscarPorId(999)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> ordenService.obtenerPorId(999))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Orden no encontrada");

            verify(ordenPort).buscarPorId(999);
        }
    }
}
