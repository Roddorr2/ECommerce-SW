package com.ecommerce.app.sales.unit;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.sales.application.dto.request.ActualizarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.request.AgregarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.CarritoResponse;
import com.ecommerce.app.sales.application.dto.response.ClienteBasicoResponse;
import com.ecommerce.app.sales.application.dto.response.EstadoCarritoResponse;
import com.ecommerce.app.sales.application.mapper.CarritoMapper;
import com.ecommerce.app.sales.application.service.CarritoService;
import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.CarritoItem;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.CarritoItemPort;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import com.ecommerce.app.sales.domain.port.ClientePort;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarritoService")
public class CarritoServiceTest {

    @Mock
    private CarritoPort carritoPort;

    @Mock
    private CarritoItemPort carritoItemPort;

    @Mock
    private ClientePort clientePort;

    @Mock
    private ProductoPort productoPort;

    @Mock
    private CarritoMapper carritoMapper;

    @InjectMocks
    private CarritoService carritoService;

    private Cliente cliente;
    private Usuario usuario;
    private Producto producto;
    private Carrito carritoActivo;
    private CarritoItem carritoItem;
    private CarritoResponse carritoResponse;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Carlos Gómez");
        usuario.setCorreo("carlos@ecommerce.com");

        cliente = new Cliente(1, usuario, "987654321", "Calle Mayor 456");

        producto = new Producto(
                10, "Zapatillas Running", "ZAP-RUN",
                "Calzado deportivo", BigDecimal.valueOf(120.00),
                15, null, true, null
        );

        carritoActivo = new Carrito(10, cliente, LocalDateTime.now(), LocalDateTime.now(), EstadoCarritoCodigo.ACTIVO);
        carritoItem = new CarritoItem(20, carritoActivo, producto, 2, BigDecimal.valueOf(120.00), LocalDateTime.now());
        
        List<CarritoItem> items = new ArrayList<>();
        items.add(carritoItem);
        carritoActivo.setItems(items);

        ClienteBasicoResponse clienteResponse = new ClienteBasicoResponse(1, "Carlos Gómez", "carlos@ecommerce.com", "987654321");
        carritoResponse = new CarritoResponse(
                10, clienteResponse, List.of(), BigDecimal.valueOf(240.00), 2,
                LocalDateTime.now(), LocalDateTime.now(), new EstadoCarritoResponse("ACTIVO")
        );
    }

    @Nested
    @DisplayName("obtenerCarritoActivo")
    class ObtenerCarritoActivo {

        @Test
        @DisplayName("dado cliente con carrito activo existente, cuando se obtiene, entonces lo retorna sin crear otro")
        void dadoClienteYCarritoExistente_cuandoObtenerCarritoActivo_entoncesRetornaExistente() {
            // Given
            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO))
                    .thenReturn(Optional.of(carritoActivo));
            when(carritoMapper.toResponse(carritoActivo)).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.obtenerCarritoActivo(1);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.id()).isEqualTo(10);
            verify(clientePort).buscarPorId(1);
            verify(carritoPort).buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO);
            verify(carritoPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cliente sin carrito activo, cuando se obtiene, entonces crea un nuevo carrito activo y lo retorna")
        void dadoClienteSinCarrito_cuandoObtenerCarritoActivo_entoncesCreaNuevoYRetorna() {
            // Given
            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO))
                    .thenReturn(Optional.empty());

            ArgumentCaptor<Carrito> captor = ArgumentCaptor.forClass(Carrito.class);
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> {
                Carrito c = inv.getArgument(0);
                c.setId(99);
                return c;
            });
            when(carritoMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.obtenerCarritoActivo(1);

            // Then
            assertThat(resultado).isNotNull();
            verify(carritoPort).guardar(captor.capture());

            Carrito nuevo = captor.getValue();
            assertThat(nuevo.getCliente()).isEqualTo(cliente);
            assertThat(nuevo.getEstadoCarrito()).isEqualTo(EstadoCarritoCodigo.ACTIVO);
            assertThat(nuevo.getFechaCreacion()).isNotNull();
            assertThat(nuevo.getFechaActualizacion()).isNotNull();
        }

        @Test
        @DisplayName("dado cliente inexistente, cuando se obtiene el carrito, entonces lanza RuntimeException")
        void dadoClienteInexistente_cuandoObtenerCarritoActivo_entoncesLanzaRuntimeException() {
            // Given
            when(clientePort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> carritoService.obtenerCarritoActivo(99))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Cliente no encontrado.");

            verify(carritoPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("agregarItem")
    class AgregarItem {

        @Test
        @DisplayName("dado producto disponible y stock suficiente, cuando se agrega un nuevo ítem, entonces lo guarda y actualiza el carrito")
        void dadoProductoDisponibleYItemNuevo_cuandoAgregarItem_entoncesGuardaItemYActualizaCarrito() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 3);
            producto.setStock(15);

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));
            when(productoPort.buscarPorId(10)).thenReturn(Optional.of(producto));
            when(carritoItemPort.buscarPorCarritoYProducto(carritoActivo, producto)).thenReturn(Optional.empty());

            ArgumentCaptor<CarritoItem> captorItem = ArgumentCaptor.forClass(CarritoItem.class);
            when(carritoItemPort.guardar(any(CarritoItem.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.agregarItem(1, request);

            // Then
            assertThat(resultado).isNotNull();
            verify(carritoItemPort).guardar(captorItem.capture());

            CarritoItem nuevo = captorItem.getValue();
            assertThat(nuevo.getCarrito()).isEqualTo(carritoActivo);
            assertThat(nuevo.getProducto()).isEqualTo(producto);
            assertThat(nuevo.getCantidad()).isEqualTo(3);
            assertThat(nuevo.getPrecioUnitario()).isEqualTo(BigDecimal.valueOf(120.00));
            assertThat(nuevo.getFechaAgregado()).isNotNull();

            verify(carritoPort).guardar(carritoActivo);
        }

        @Test
        @DisplayName("dado ítem ya existente en el carrito y stock suficiente, cuando se agrega el mismo producto, entonces incrementa la cantidad")
        void dadoItemExistenteYStockSuficiente_cuandoAgregarItem_entoncesIncrementaCantidad() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 3);
            producto.setStock(15);
            carritoItem.setCantidad(2); // cantidad previa

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));
            when(productoPort.buscarPorId(10)).thenReturn(Optional.of(producto));
            when(carritoItemPort.buscarPorCarritoYProducto(carritoActivo, producto)).thenReturn(Optional.of(carritoItem));

            when(carritoItemPort.guardar(any(CarritoItem.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.agregarItem(1, request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(carritoItem.getCantidad()).isEqualTo(5); // 2 previos + 3 agregados
            verify(carritoItemPort).guardar(carritoItem);
            verify(carritoPort).guardar(carritoActivo);
        }

        @Test
        @DisplayName("dado producto no disponible, cuando se agrega, entonces lanza IllegalStateException")
        void dadoProductoNoDisponible_cuandoAgregarItem_entoncesLanzaIllegalStateException() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 2);
            producto.setActivo(false); // No disponible

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));
            when(productoPort.buscarPorId(10)).thenReturn(Optional.of(producto));

            // When / Then
            assertThatThrownBy(() -> carritoService.agregarItem(1, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El producto no está disponible.");

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado stock insuficiente para nuevo ítem, cuando se agrega, entonces lanza IllegalStateException")
        void dadoStockInsuficienteParaNuevo_cuandoAgregarItem_entoncesLanzaIllegalStateException() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 10);
            producto.setStock(5); // requerido 10, disponible 5

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));
            when(productoPort.buscarPorId(10)).thenReturn(Optional.of(producto));
            when(carritoItemPort.buscarPorCarritoYProducto(carritoActivo, producto)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> carritoService.agregarItem(1, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Stock insuficiente. Disponible: " + producto.getSku());

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado stock insuficiente sumando cantidad existente, cuando se agrega, entonces lanza IllegalStateException")
        void dadoStockInsuficienteParaExistente_cuandoAgregarItem_entoncesLanzaIllegalStateException() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 5);
            producto.setStock(6);
            carritoItem.setCantidad(2); // requerido total 2 + 5 = 7, disponible 6

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));
            when(productoPort.buscarPorId(10)).thenReturn(Optional.of(producto));
            when(carritoItemPort.buscarPorCarritoYProducto(carritoActivo, producto)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.agregarItem(1, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Stock insuficiente. Disponible: " + producto.getSku());

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado carrito no modificable, cuando se agrega un producto, entonces lanza IllegalStateException")
        void dadoCarritoNoModificable_cuandoAgregarItem_entoncesLanzaIllegalStateException() {
            // Given
            AgregarItemCarritoRequest request = new AgregarItemCarritoRequest(10, 1);
            carritoActivo.setEstadoCarrito(EstadoCarritoCodigo.CONVERTIDO); // no es activo/modificable

            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));

            // When / Then
            assertThatThrownBy(() -> carritoService.agregarItem(1, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El carrito no permite modificaciones en este estado.");

            verify(carritoItemPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("actualizarItem")
    class ActualizarItem {

        @Test
        @DisplayName("dado ítem válido y stock suficiente, cuando se actualiza la cantidad, entonces la cambia y actualiza la fecha de modificación")
        void dadoItemYStockValidos_cuandoActualizarItem_entoncesActualizaYGuarda() {
            // Given
            ActualizarItemCarritoRequest request = new ActualizarItemCarritoRequest(5);
            producto.setStock(10); // suficiente para 5

            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));
            when(carritoItemPort.guardar(any(CarritoItem.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.actualizarItem(1, 20, request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(carritoItem.getCantidad()).isEqualTo(5);
            verify(carritoItemPort).guardar(carritoItem);
            verify(carritoPort).guardar(carritoActivo);
        }

        @Test
        @DisplayName("dado ítem que pertenece a otro cliente, cuando se actualiza, entonces lanza IllegalStateException")
        void dadoItemDeOtroCliente_cuandoActualizarItem_entoncesLanzaIllegalStateException() {
            // Given
            ActualizarItemCarritoRequest request = new ActualizarItemCarritoRequest(5);
            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.actualizarItem(99, 20, request)) // cliente es 99, owner es 1
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Este ítem no pertenece al cliente.");

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado carrito no modificable, cuando se actualiza el ítem, entonces lanza IllegalStateException")
        void dadoCarritoNoModificable_cuandoActualizarItem_entoncesLanzaIllegalStateException() {
            // Given
            ActualizarItemCarritoRequest request = new ActualizarItemCarritoRequest(5);
            carritoActivo.setEstadoCarrito(EstadoCarritoCodigo.ABANDONADO); // no mutable

            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.actualizarItem(1, 20, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El carrito no permite modificaciones en este estado.");

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado stock insuficiente en producto, cuando se actualiza el ítem, entonces lanza IllegalStateException")
        void dadoStockInsuficiente_cuandoActualizarItem_entoncesLanzaIllegalStateException() {
            // Given
            ActualizarItemCarritoRequest request = new ActualizarItemCarritoRequest(10);
            producto.setStock(4); // requerido 10, stock es 4

            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.actualizarItem(1, 20, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Stock insuficiente. Disponible: 4");

            verify(carritoItemPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cantidad inválida (cero o menor), cuando se actualiza, entonces lanza IllegalArgumentException")
        void dadoCantidadInvalida_cuandoActualizarItem_entoncesLanzaIllegalArgumentException() {
            // Given
            ActualizarItemCarritoRequest request = new ActualizarItemCarritoRequest(0); // invalid

            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.actualizarItem(1, 20, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("La cantidad debe ser mayor a 0.");

            verify(carritoItemPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("eliminarItem")
    class EliminarItem {

        @Test
        @DisplayName("dado ítem válido, cuando se elimina del carrito, entonces borra el ítem y actualiza fecha del carrito")
        void dadoItemValido_cuandoEliminarItem_entoncesBorraYGuarda() {
            // Given
            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));
            when(carritoPort.guardar(any(Carrito.class))).thenAnswer(inv -> inv.getArgument(0));
            when(carritoMapper.toResponse(any(Carrito.class))).thenReturn(carritoResponse);

            // When
            CarritoResponse resultado = carritoService.eliminarItem(1, 20);

            // Then
            assertThat(resultado).isNotNull();
            verify(carritoItemPort).eliminar(carritoItem);
            verify(carritoPort).guardar(carritoActivo);
        }

        @Test
        @DisplayName("dado ítem de otro cliente, cuando se elimina, entonces lanza IllegalStateException")
        void dadoItemDeOtroCliente_cuandoEliminarItem_entoncesLanzaIllegalStateException() {
            // Given
            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.eliminarItem(99, 20))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Este ítem no pertenece al cliente.");

            verify(carritoItemPort, never()).eliminar(any());
        }

        @Test
        @DisplayName("dado carrito no modificable, cuando se elimina el ítem, entonces lanza IllegalStateException")
        void dadoCarritoNoModificable_cuandoEliminarItem_entoncesLanzaIllegalStateException() {
            // Given
            carritoActivo.setEstadoCarrito(EstadoCarritoCodigo.CONVERTIDO);
            when(carritoItemPort.buscarPorId(20)).thenReturn(Optional.of(carritoItem));

            // When / Then
            assertThatThrownBy(() -> carritoService.eliminarItem(1, 20))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El carrito no permite modificaciones en este estado.");

            verify(carritoItemPort, never()).eliminar(any());
        }
    }

    @Nested
    @DisplayName("vaciarCarrito")
    class VaciarCarrito {

        @Test
        @DisplayName("dado carrito activo, cuando se vacía, entonces elimina todos los ítems asociados")
        void dadoCarritoActivo_cuandoVaciarCarrito_entoncesEliminaTodosLosItems() {
            // Given
            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));

            // When
            carritoService.vaciarCarrito(1);

            // Then
            verify(carritoItemPort).eliminarTodos(carritoActivo.getItems());
            verify(carritoPort).guardar(carritoActivo);
        }

        @Test
        @DisplayName("dado carrito no modificable, cuando se intenta vaciar, entonces lanza IllegalStateException")
        void dadoCarritoNoModificable_cuandoVaciarCarrito_entoncesLanzaIllegalStateException() {
            // Given
            carritoActivo.setEstadoCarrito(EstadoCarritoCodigo.CONVERTIDO);
            when(clientePort.buscarPorId(1)).thenReturn(Optional.of(cliente));
            when(carritoPort.buscarPorClienteYEstado(cliente, EstadoCarritoCodigo.ACTIVO)).thenReturn(Optional.of(carritoActivo));

            // When / Then
            assertThatThrownBy(() -> carritoService.vaciarCarrito(1))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El carrito no permite modificaciones en este estado.");

            verify(carritoItemPort, never()).eliminarTodos(any());
            verify(carritoPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cliente inexistente, cuando se vacía, entonces lanza EntityNotFoundException")
        void dadoClienteInexistente_cuandoVaciarCarrito_entoncesLanzaEntityNotFoundException() {
            // Given
            when(clientePort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> carritoService.vaciarCarrito(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Cliente no encontrado.");

            verify(carritoItemPort, never()).eliminarTodos(any());
        }
    }
}
