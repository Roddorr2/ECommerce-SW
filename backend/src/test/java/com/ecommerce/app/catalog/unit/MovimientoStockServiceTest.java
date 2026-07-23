package com.ecommerce.app.catalog.unit;

import com.ecommerce.app.catalog.application.dto.response.MovimientoStockResponse;
import com.ecommerce.app.catalog.application.mapper.MovimientoStockMapper;
import com.ecommerce.app.catalog.application.service.MovimientoStockService;
import com.ecommerce.app.catalog.domain.enums.TipoMovimientoCodigo;
import com.ecommerce.app.catalog.domain.enums.TipoReferenciaCodigo;
import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.MovimientoStockPort;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovimientoStockService")
public class MovimientoStockServiceTest {

    @Mock
    private MovimientoStockPort movimientoStockPort;

    @Mock
    private ProductoPort productoPort;

    @Mock
    private MovimientoStockMapper movimientoStockMapper;

    @InjectMocks
    private MovimientoStockService movimientoStockService;

    private Producto producto;
    private Usuario usuario;
    private MovimientoStock movimientoSalida;
    private MovimientoStock movimientoEntrada;
    private MovimientoStockResponse responseSalida;
    private MovimientoStockResponse responseEntrada;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Electrónica");

        producto = new Producto(
                1, "Disco SSD 480GB", "DISCO-480",
                "Disco de estado sólido", BigDecimal.valueOf(250.00),
                10, null, true, categoria
        );

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan.perez@ecommerce.com");

        movimientoSalida = new MovimientoStock(
                1, producto, 10, 5,
                TipoMovimientoCodigo.SALIDA, TipoReferenciaCodigo.ORDEN,
                "ORD-123", usuario, LocalDateTime.now(), "Venta registrada - Orden #ORD-123"
        );

        movimientoEntrada = new MovimientoStock(
                2, producto, 10, 15,
                TipoMovimientoCodigo.ENTRADA, TipoReferenciaCodigo.COMPRA,
                "COM-456", usuario, LocalDateTime.now(), "Compra recibida - Compra #COM-456"
        );

        responseSalida = new MovimientoStockResponse(
                1, "Disco SSD 480GB", "DISCO-480", 10, 5, -5,
                "SALIDA", "ORDEN", "ORD-123", "Juan Pérez", LocalDateTime.now(),
                "Venta registrada - Orden #ORD-123"
        );

        responseEntrada = new MovimientoStockResponse(
                2, "Disco SSD 480GB", "DISCO-480", 10, 15, 5,
                "ENTRADA", "COMPRA", "COM-456", "Juan Pérez", LocalDateTime.now(),
                "Compra recibida - Compra #COM-456"
        );
    }

    @Nested
    @DisplayName("obtenerMovimientos")
    class ObtenerMovimientos {

        @Test
        @DisplayName("dado ID existente, cuando se obtienen movimientos, entonces retorna la lista mapeada ordenada")
        void dadoIdExistente_cuandoObtenerMovimientos_entoncesRetornaListaMapeada() {
            // Given
            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));
            when(movimientoStockPort.buscarPorProductoOrdenado(producto))
                    .thenReturn(List.of(movimientoSalida, movimientoEntrada));
            when(movimientoStockMapper.toResponse(movimientoSalida)).thenReturn(responseSalida);
            when(movimientoStockMapper.toResponse(movimientoEntrada)).thenReturn(responseEntrada);

            // When
            List<MovimientoStockResponse> resultado = movimientoStockService.obtenerMovimientos(1);

            // Then
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).id()).isEqualTo(1);
            assertThat(resultado.get(1).id()).isEqualTo(2);
            verify(productoPort).buscarPorId(1);
            verify(movimientoStockPort).buscarPorProductoOrdenado(producto);
            verify(movimientoStockMapper).toResponse(movimientoSalida);
            verify(movimientoStockMapper).toResponse(movimientoEntrada);
        }

        @Test
        @DisplayName("dado ID existente sin movimientos, cuando se obtienen movimientos, entonces retorna lista vacía")
        void dadoIdExistenteSinMovimientos_cuandoObtenerMovimientos_entoncesRetornaListaVacia() {
            // Given
            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));
            when(movimientoStockPort.buscarPorProductoOrdenado(producto))
                    .thenReturn(List.of());

            // When
            List<MovimientoStockResponse> resultado = movimientoStockService.obtenerMovimientos(1);

            // Then
            assertThat(resultado).isEmpty();
            verify(productoPort).buscarPorId(1);
            verify(movimientoStockPort).buscarPorProductoOrdenado(producto);
            verifyNoInteractions(movimientoStockMapper);
        }

        @Test
        @DisplayName("dado ID inexistente, cuando se obtienen movimientos, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_cuandoObtenerMovimientos_entoncesLanzaEntityNotFoundException() {
            // Given
            when(productoPort.buscarPorId(99)).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.obtenerMovimientos(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Producto no encontrado.");

            verify(productoPort).buscarPorId(99);
            verify(movimientoStockPort, never()).buscarPorProductoOrdenado(any());
            verifyNoInteractions(movimientoStockMapper);
        }
    }

    @Nested
    @DisplayName("registrarSalidaPorOrden")
    class RegistrarSalidaPorOrden {

        @Test
        @DisplayName("dado datos válidos, cuando se registra salida por orden, entonces guarda y retorna el movimiento")
        void dadoDatosValidos_cuandoRegistrarSalidaPorOrden_entoncesGuardaYRetornaMovimiento() {
            // Given
            producto.setStock(10);
            int cantidad = 4;
            String codigoOrden = "ORD-789";

            ArgumentCaptor<MovimientoStock> captor = ArgumentCaptor.forClass(MovimientoStock.class);
            when(movimientoStockPort.guardar(any(MovimientoStock.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            MovimientoStock resultado = movimientoStockService.registrarSalidaPorOrden(producto, cantidad, codigoOrden, usuario);

            // Then
            assertThat(resultado).isNotNull();
            verify(movimientoStockPort).guardar(captor.capture());

            MovimientoStock guardado = captor.getValue();
            assertThat(guardado.getProducto()).isEqualTo(producto);
            assertThat(guardado.getCantidadAnterior()).isEqualTo(10);
            assertThat(guardado.getCantidadNueva()).isEqualTo(6);
            assertThat(guardado.getTipoMovimiento()).isEqualTo(TipoMovimientoCodigo.SALIDA);
            assertThat(guardado.getTipoReferencia()).isEqualTo(TipoReferenciaCodigo.ORDEN);
            assertThat(guardado.getCodigoReferencia()).isEqualTo(codigoOrden);
            assertThat(guardado.getUsuario()).isEqualTo(usuario);
            assertThat(guardado.getFechaMovimiento()).isNotNull();
            assertThat(guardado.getObservacion()).isEqualTo("Venta registrada - Orden #ORD-789");
        }

        @Test
        @DisplayName("dado cantidad cero, cuando se registra salida por orden, entonces lanza IllegalStateException")
        void dadoCantidadCero_cuandoRegistrarSalidaPorOrden_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 0;
            String codigoOrden = "ORD-789";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarSalidaPorOrden(producto, cantidad, codigoOrden, usuario))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de SALIDA debe tener diferencia negativa.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cantidad negativa, cuando se registra salida por orden, entonces lanza IllegalStateException")
        void dadoCantidadNegativa_cuandoRegistrarSalidaPorOrden_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = -3;
            String codigoOrden = "ORD-789";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarSalidaPorOrden(producto, cantidad, codigoOrden, usuario))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de SALIDA debe tener diferencia negativa.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado usuario nulo, cuando se registra salida por orden, entonces lanza IllegalStateException")
        void dadoUsuarioNulo_cuandoRegistrarSalidaPorOrden_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 2;
            String codigoOrden = "ORD-789";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarSalidaPorOrden(producto, cantidad, codigoOrden, null))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El movimiento debe tener un usuario responsable");

            verify(movimientoStockPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("registrarEntradaPorCompra")
    class RegistrarEntradaPorCompra {

        @Test
        @DisplayName("dado datos válidos, cuando se registra entrada por compra, entonces guarda y retorna el movimiento")
        void dadoDatosValidos_cuandoRegistrarEntradaPorCompra_entoncesGuardaYRetornaMovimiento() {
            // Given
            producto.setStock(10);
            int cantidad = 5;
            String codigoCompra = "COM-999";

            ArgumentCaptor<MovimientoStock> captor = ArgumentCaptor.forClass(MovimientoStock.class);
            when(movimientoStockPort.guardar(any(MovimientoStock.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            MovimientoStock resultado = movimientoStockService.registrarEntradaPorCompra(producto, cantidad, codigoCompra, usuario);

            // Then
            assertThat(resultado).isNotNull();
            verify(movimientoStockPort).guardar(captor.capture());

            MovimientoStock guardado = captor.getValue();
            assertThat(guardado.getProducto()).isEqualTo(producto);
            assertThat(guardado.getCantidadAnterior()).isEqualTo(10);
            assertThat(guardado.getCantidadNueva()).isEqualTo(15);
            assertThat(guardado.getTipoMovimiento()).isEqualTo(TipoMovimientoCodigo.ENTRADA);
            assertThat(guardado.getTipoReferencia()).isEqualTo(TipoReferenciaCodigo.COMPRA);
            assertThat(guardado.getCodigoReferencia()).isEqualTo(codigoCompra);
            assertThat(guardado.getUsuario()).isEqualTo(usuario);
            assertThat(guardado.getFechaMovimiento()).isNotNull();
            assertThat(guardado.getObservacion()).isEqualTo("Compra recibida - Compra #COM-999");
        }

        @Test
        @DisplayName("dado cantidad cero, cuando se registra entrada por compra, entonces lanza IllegalStateException")
        void dadoCantidadCero_cuandoRegistrarEntradaPorCompra_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 0;
            String codigoCompra = "COM-999";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarEntradaPorCompra(producto, cantidad, codigoCompra, usuario))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de ENTRADA debe tener diferencia positiva.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cantidad negativa, cuando se registra entrada por compra, entonces lanza IllegalStateException")
        void dadoCantidadNegativa_cuandoRegistrarEntradaPorCompra_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = -5;
            String codigoCompra = "COM-999";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarEntradaPorCompra(producto, cantidad, codigoCompra, usuario))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de ENTRADA debe tener diferencia positiva.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado usuario nulo, cuando se registra entrada por compra, entonces lanza IllegalStateException")
        void dadoUsuarioNulo_cuandoRegistrarEntradaPorCompra_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 5;
            String codigoCompra = "COM-999";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarEntradaPorCompra(producto, cantidad, codigoCompra, null))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El movimiento debe tener un usuario responsable");

            verify(movimientoStockPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("registrarDevolucion")
    class RegistrarDevolucion {

        @Test
        @DisplayName("dado datos válidos con motivo, cuando se registra devolución, entonces guarda y retorna el movimiento con motivo en la observación")
        void dadoDatosValidosConMotivo_cuandoRegistrarDevolucion_entoncesGuardaYRetornaMovimientoConMotivo() {
            // Given
            producto.setStock(10);
            int cantidad = 2;
            String codigoOrden = "ORD-456";
            String motivo = "Producto fallado";

            ArgumentCaptor<MovimientoStock> captor = ArgumentCaptor.forClass(MovimientoStock.class);
            when(movimientoStockPort.guardar(any(MovimientoStock.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            MovimientoStock resultado = movimientoStockService.registrarDevolucion(producto, cantidad, codigoOrden, usuario, motivo);

            // Then
            assertThat(resultado).isNotNull();
            verify(movimientoStockPort).guardar(captor.capture());

            MovimientoStock guardado = captor.getValue();
            assertThat(guardado.getProducto()).isEqualTo(producto);
            assertThat(guardado.getCantidadAnterior()).isEqualTo(10);
            assertThat(guardado.getCantidadNueva()).isEqualTo(12);
            assertThat(guardado.getTipoMovimiento()).isEqualTo(TipoMovimientoCodigo.ENTRADA);
            assertThat(guardado.getTipoReferencia()).isEqualTo(TipoReferenciaCodigo.DEVOLUCION);
            assertThat(guardado.getCodigoReferencia()).isEqualTo(codigoOrden);
            assertThat(guardado.getUsuario()).isEqualTo(usuario);
            assertThat(guardado.getFechaMovimiento()).isNotNull();
            assertThat(guardado.getObservacion()).isEqualTo("Devolución - Orden #ORD-456 - Producto fallado");
        }

        @Test
        @DisplayName("dado datos válidos sin motivo, cuando se registra devolución, entonces guarda y retorna el movimiento indicando sin motivo")
        void dadoDatosValidosSinMotivo_cuandoRegistrarDevolucion_entoncesGuardaYRetornaMovimientoSinMotivo() {
            // Given
            producto.setStock(10);
            int cantidad = 3;
            String codigoOrden = "ORD-456";

            ArgumentCaptor<MovimientoStock> captor = ArgumentCaptor.forClass(MovimientoStock.class);
            when(movimientoStockPort.guardar(any(MovimientoStock.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            MovimientoStock resultado = movimientoStockService.registrarDevolucion(producto, cantidad, codigoOrden, usuario, null);

            // Then
            assertThat(resultado).isNotNull();
            verify(movimientoStockPort).guardar(captor.capture());

            MovimientoStock guardado = captor.getValue();
            assertThat(guardado.getObservacion()).isEqualTo("Devolución - Orden #ORD-456 - Sin motivo");
        }

        @Test
        @DisplayName("dado cantidad cero, cuando se registra devolución, entonces lanza IllegalStateException")
        void dadoCantidadCero_cuandoRegistrarDevolucion_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 0;
            String codigoOrden = "ORD-456";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarDevolucion(producto, cantidad, codigoOrden, usuario, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de ENTRADA debe tener diferencia positiva.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado cantidad negativa, cuando se registra devolución, entonces lanza IllegalStateException")
        void dadoCantidadNegativa_cuandoRegistrarDevolucion_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = -2;
            String codigoOrden = "ORD-456";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarDevolucion(producto, cantidad, codigoOrden, usuario, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Un movimiento de ENTRADA debe tener diferencia positiva.");

            verify(movimientoStockPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado usuario nulo, cuando se registra devolución, entonces lanza IllegalStateException")
        void dadoUsuarioNulo_cuandoRegistrarDevolucion_entoncesLanzaIllegalStateException() {
            // Given
            producto.setStock(10);
            int cantidad = 1;
            String codigoOrden = "ORD-456";

            // When / Then
            assertThatThrownBy(() -> movimientoStockService.registrarDevolucion(producto, cantidad, codigoOrden, null, "Motivo"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("El movimiento debe tener un usuario responsable");

            verify(movimientoStockPort, never()).guardar(any());
        }
    }
}
