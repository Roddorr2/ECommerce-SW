package com.ecommerce.app.catalog.unit;

import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Producto - lógica de dominio")
public class ProductoTest {

    private Producto producto;
    private Categoria categoria;

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
    }

    @Nested
    @DisplayName("reducirStock")
    class ReducirStock {

        @Test
        @DisplayName("dado stock suficiente, cuando se reduce, entonces el stock disminuye")
        void dadoStockInsuficiente_cuandoReducir_entoncesDisminuye() {
            producto.reducirStock(3);
            assertThat(producto.getStock()).isEqualTo(7);
        }

        @Test
        @DisplayName("dado stock exacto, cuando se reduce con la cantidad exacta, entonces el stock queda en cero")
        void dadoStockExacto_cuandoReducirCantidadExacta_entoncesStockCero() {
            producto.reducirStock(10);
            assertThat(producto.getStock()).isZero();
        }

        @Test
        @DisplayName("dado stock insuficiente, cuando se reduce, entonces lanza IllegalArgumentException")
        void dadoStockInsuficiente_cuandoReducir_entoncesLanzaException() {
            assertThatThrownBy(() -> producto.reducirStock(11))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Stock insuficiente")
                    .hasMessageContaining("10")
                    .hasMessageContaining("11");
        }

        @Test
        @DisplayName("dado stock cero, cuando se intenta reducir, entonces lanza IllegalArgumentException")
        void dadoStockCero_cuandoReducir_entoncesLanzaExcepcion() {
            producto.setStock(0);
            assertThatThrownBy(() -> producto.reducirStock(1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("incrementarStock")
    class IncrementarStock {
        @Test
        @DisplayName("dada cantidad positiva, cuando se incrementa, entonces el stock aumenta correctamente")
        void dadaCantidadPositiva_cuandoIncrementar_entoncesAumenta() {
            producto.incrementarStock(5);
            assertThat(producto.getStock()).isEqualTo(15);
        }

        @Test
        @DisplayName("dada cantidad cero, cuando se incrementa, entonces lanza IllegalArgumentException")
        void dadaCantidadCero_cuandoIncrementar_entoncesLanzaExcepcion() {
            assertThatThrownBy(() -> producto.incrementarStock(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positiva");
        }

        @Test
        @DisplayName("dada cantidad negativa, cuando se incrementa, entonces lanza IllegalArgumentException")
        void dadaCantidadNegativa_cuandoIncrementar_entoncesLanzaExcepcion() {
            assertThatThrownBy(() -> producto.incrementarStock(-5))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("tieneStockSuficiente")
    class tieneStockSuficiente {

        @Test
        @DisplayName("dado stock mayor a la cantidad, entonces retorna true")
        void dadoStockMayor_entoncesTrue() {
            assertThat(producto.tieneStockSuficiente(5)).isTrue();
        }

        @Test
        @DisplayName("dado stock igual a la cantidad, entonces retorna true")
        void dadoStockIgual_entoncesTrue() {
            assertThat(producto.tieneStockSuficiente(10)).isTrue();
        }

        @Test
        @DisplayName("dado stock menor a la cantidad, entonces retorna false")
        void dadoStockMenor_entoncesFalse() {
            assertThat(producto.tieneStockSuficiente(11)).isFalse();
        }
    }

    @Nested
    @DisplayName("isDisponible")
    class IsDisponible {

        @Test
        @DisplayName("dado producto activo con stock, entonces está disponible")
        void dadoActivoConStock_entoncesDisponible() {
            assertThat(producto.isDisponible()).isTrue();
        }

        @Test
        @DisplayName("dado producto inactivo con stock, entonces no está disponible")
        void dadoInactivoConStock_entoncesNoDisponible() {
            producto.setActivo(false);
            assertThat(producto.isDisponible()).isFalse();
        }

        @Test
        @DisplayName("dado producto activo sin stock, entonces no está disponible")
        void dadoActivoSinStock_entoncesNoDisponible() {
            producto.setStock(0);
            assertThat(producto.isDisponible()).isFalse();
        }
    }

    @Nested
    @DisplayName("calcularValorInventario")
    class CalcularValorInventario {

        @Test
        @DisplayName("dado precio y stock, cuando se calcula, entonces retorna precio x stock")
        void dadoPrecioYStock_cuandoCalcular_entoncesRetornaPrecioXStock() {
            BigDecimal valor = producto.calcularValorInventario();
            assertThat(valor).isEqualByComparingTo(BigDecimal.valueOf(2500.00));
        }

        @Test
        @DisplayName("dado stock cero, cuando se calcula, entonces retorna cero")
        void dadoStockCero_cuandoCalcular_entoncesRetornaCero() {
            producto.setStock(0);
            assertThat(producto.calcularValorInventario())
                    .isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("requiereReposicion")
    class RequiereReposicion {

        @Test
        @DisplayName("dado stock igual al mínimo, entonces requiere reposición")
        void dadoStockIgualMinimo_entoncesRequiereReposicion() {
            assertThat(producto.requiereReposicion(10)).isTrue();
        }

        @Test
        @DisplayName("dado stock mayor al mínimo, entonces no requiere reposición")
        void dadoStockMayorMinimo_entoncesNoRequiereReposicion() {
            assertThat(producto.requiereReposicion(5)).isFalse();
        }

        @Test
        @DisplayName("dado stock menor al mínimo, entonces requiere reposición")
        void dadoStockMenorMinimo_entoncesRequiereReposicion() {
            producto.setStock(3);
            assertThat(producto.requiereReposicion(5)).isTrue();
        }
    }
}