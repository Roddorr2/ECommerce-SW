package com.ecommerce.app.catalog.unit;

import com.ecommerce.app.catalog.application.dto.request.ActualizarProductoRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearProductoRequest;
import com.ecommerce.app.catalog.application.dto.response.ProductoResponse;
import com.ecommerce.app.catalog.application.mapper.ProductoMapper;
import com.ecommerce.app.catalog.application.service.ProductoService;
import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.catalog.domain.port.CategoriaPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService")
public class ProductoServiceTest {

    @Mock private ProductoPort productoPort;
    @Mock private CategoriaPort categoriaPort;
    @Mock private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Categoria categoria;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Electrónica");

        producto = new Producto(
                1, "Disco SSD 480GB", "DISCO-480",
                "Disco de estado sólido", BigDecimal.valueOf(250.00),
                10, null, true, categoria
        );

        productoResponse = new ProductoResponse(
                1, "Disco SSD 480GB", "DISCO-480",
                "Disco de estado sólido", BigDecimal.valueOf(250.00),
                10, null, true, 1, "Electrónica"
        );
    }

    @Nested
    @DisplayName("listarProductos")
    class ListarProductos {

        @Test
        @DisplayName("cuando existen productos, entonces retorna la lista mapeada")
        void cuandoExistenProductos_entoncesRetornaLista() {
            when(productoPort.listarTodos()).thenReturn(List.of(producto));
            when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

            List<ProductoResponse> resultado = productoService.listarProductos();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).id()).isEqualTo(1);
            verify(productoPort).listarTodos();
        }

        @Test
        @DisplayName("cuando no existen productos, entonces retorna lista vacía")
        void cuandoNoexistenProductos_entoncesRetornaListaVacia() {
            when(productoPort.listarTodos()).thenReturn(List.of());

            List<ProductoResponse> resultado = productoService.listarProductos();

            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("obtenerPorId")
    class ObtenerPorId {

        @Test
        @DisplayName("dado ID existente, entonces retorna el producto")
        void dadoIdExistente_entoncesRetornaProducto() {
            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));
            when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

            ProductoResponse resultado = productoService.obtenerPorId(1);

            assertThat(resultado.id()).isEqualTo(1);
            assertThat(resultado.nombre()).isEqualTo("Disco SSD 480GB");
        }

        @Test
        @DisplayName("dado ID inexistente, entonces lanza EntityNotFoundException")
        void dadoIdInexistente_entoncesLanzaExcepcion() {
            when(productoPort.buscarPorId(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productoService.obtenerPorId(99))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("registrarProducto")
    class RegistrarProducto {

        @Test
        @DisplayName("dado request válido, cuando se registra, entonces guarda y restorna el producto")
        void dadoRequestValido_cuandoRegistrar_entoncesGuarda() {
            CrearProductoRequest request = new CrearProductoRequest(
                    "Disco SSD 480GB", "DISCO-480", "Descripción",
                    BigDecimal.valueOf(250.00), null, 1
            );

            when(categoriaPort.buscarPorId(1)).thenReturn(Optional.of(categoria));
            when(productoPort.buscarPorSku("DISCO-480")).thenReturn(Optional.empty());
            when(productoMapper.toEntity(request, categoria)).thenReturn(producto);
            when(productoPort.guardar(producto)).thenReturn(producto);
            when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

            ProductoResponse resultado = productoService.registrarProducto(request);

            assertThat(resultado.sku()).isEqualTo("DISCO-480");
            verify(productoPort).guardar(producto);
        }

        @Test
        @DisplayName("dado SKU duplicado, cuando se registra, entonces lanza IllegalArgumentException")
        void dadoSkuDuplicado_cuandoRegistrar_entoncesLanzaExcepcion() {
            CrearProductoRequest request = new CrearProductoRequest(
                    "Otro disco", "DISCO-480", "Descripción",
                    BigDecimal.valueOf(200.00), null, 1
            );

            when(categoriaPort.buscarPorId(1)).thenReturn(Optional.of(categoria));
            when(productoPort.buscarPorSku("DISCO-480")).thenReturn(Optional.of(producto));

            assertThatThrownBy(() -> productoService.registrarProducto(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DISCO-480");

            verify(productoPort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado categoría inexistente, cuando se registra, entonces lanza IllegalArgumentException")
        void dadoCategoriaInexistente_cuandoRegistrar_entoncesLanzaExcepcion() {
            CrearProductoRequest request = new CrearProductoRequest(
                    "Disco", "DISCO-999", "Descripción",
                    BigDecimal.valueOf(200.00), null, 99
            );

            when(categoriaPort.buscarPorId(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productoService.registrarProducto(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("99");

            verify(productoPort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("editarProducto")
    class EditarProducto {

        @Test
        @DisplayName("dado request válido, cuando se edita, entonces actualiza y retorna el producto")
        void dadoRequestValido_cuandoEditar_entoncesActualiza() {
            ActualizarProductoRequest request = new ActualizarProductoRequest(
                    1, "Disco SSD 960GB", "DISCO-480",
                    "Descripción actualizada", BigDecimal.valueOf(350.00),
                    null, true, 1
            );

            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));
            when(categoriaPort.buscarPorId(1)).thenReturn(Optional.of(categoria));
            when(productoPort.buscarPorSku("DISCO-480")).thenReturn(Optional.of(producto));
            when(productoPort.guardar(producto)).thenReturn(producto);
            when(productoMapper.toResponse(producto)).thenReturn(productoResponse);

            ProductoResponse resultado = productoService.editarProducto(request);

            assertThat(resultado).isNotNull();
            verify(productoMapper).updateEntity(producto, request, categoria);
            verify(productoPort).guardar(producto);
        }

        @Test
        @DisplayName("dado SKU de otro producto, cuando se edita, entonces lanza IllegalArgumentException")
        void dadoSkuDeOtroProducto_cuandoEditar_entoncesLanzaExcepcion() {
            Producto otroProducto = new Producto(
                    2, "Otro producto", "DISCO-480",
                    "Otro", BigDecimal.valueOf(100.00),
                    5, null, true, categoria
            );

            ActualizarProductoRequest request = new ActualizarProductoRequest(
                    1, "Disco SSD 960GB", "DISCO-480",
                    "Descripción", BigDecimal.valueOf(350.00),
                    null, true, 1
            );

            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));
            when(categoriaPort.buscarPorId(1)).thenReturn(Optional.of(categoria));
            when(productoPort.buscarPorSku("DISCO-480")).thenReturn(Optional.of(otroProducto));

            assertThatThrownBy(() -> productoService.editarProducto(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DISCO-480");
        }
    }

    @Nested
    @DisplayName("eliminarProducto")
    class EliminarProducto {

        @Test
        @DisplayName("dado producto sin asociaciones, cuando se elimina, entonces se borra correctamente")
        void dadoProductoSinAsociaciones_cuandoEliminar_entoncesSeBorra() {
            when(productoPort.buscarPorId(1)).thenReturn(Optional.of(producto));

            productoService.eliminarProducto(1);

            verify(productoPort).eliminar(producto);
        }

        @Test
        @DisplayName("dado producto inexistente, cuando se elimina, entonces lanza EntityNotFoundException")
        void dadoProductoInexistente_cuandoEliminar_entoncesLanzaExcepcion() {
            when(productoPort.buscarPorId(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productoService.eliminarProducto(99))
                    .isInstanceOf(EntityNotFoundException.class);

            verify(productoPort, never()).eliminar(any());
        }
    }

}