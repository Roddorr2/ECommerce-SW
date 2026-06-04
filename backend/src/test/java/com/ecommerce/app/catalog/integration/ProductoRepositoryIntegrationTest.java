package com.ecommerce.app.catalog.integration;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.CategoriaEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.repository.ProductoJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ProductoRepository Integration")
public class ProductoRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoJpaRepository productoRepository;

    @Nested
    @DisplayName("buscarProductosDisponibles")
    class BuscarProductosDisponibles {

        @Test
        @DisplayName("cuando existen productos disponibles con coincidencia de nombre, entonces los retorna")
        void cuandoProductosDisponiblesYCoincideNombre_entoncesLosRetorna() {
            // Given
            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setNombre("Deportes");
            entityManager.persist(categoria);

            ProductoEntity p1 = new ProductoEntity();
            p1.setNombre("Zapatillas Running Nike");
            p1.setSku("NIKE-ZAP");
            p1.setDescripcion("Desc");
            p1.setPrecio(BigDecimal.valueOf(150));
            p1.setStock(10);
            p1.setActivo(true);
            p1.setCategoriaEntity(categoria);

            ProductoEntity p2 = new ProductoEntity();
            p2.setNombre("Nike T-Shirt");
            p2.setSku("NIKE-TSH");
            p2.setDescripcion("Desc");
            p2.setPrecio(BigDecimal.valueOf(50));
            p2.setStock(0); // stock = 0
            p2.setActivo(true);
            p2.setCategoriaEntity(categoria);

            ProductoEntity p3 = new ProductoEntity();
            p3.setNombre("Pelota de Fútbol");
            p3.setSku("PEL-FUT");
            p3.setDescripcion("Desc");
            p3.setPrecio(BigDecimal.valueOf(30));
            p3.setStock(5);
            p3.setActivo(true);
            p3.setCategoriaEntity(categoria);

            entityManager.persist(p1);
            entityManager.persist(p2);
            entityManager.persist(p3);
            entityManager.flush();

            // When
            List<ProductoEntity> resultado = productoRepository.buscarProductosDisponibles("nike");

            // Then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getSku()).isEqualTo("NIKE-ZAP");
        }

        @Test
        @DisplayName("cuando no hay productos con coincidencia, entonces retorna lista vacía")
        void cuandoNoHayCoincidencia_entoncesRetornaListaVacia() {
            // When
            List<ProductoEntity> resultado = productoRepository.buscarProductosDisponibles("inexistente");

            // Then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBySku")
    class FindBySku {

        @Test
        @DisplayName("dado SKU existente, cuando se busca, entonces retorna el producto opcional conteniendo el valor")
        void dadoSkuExistente_cuandoSeBusca_entoncesRetornaProducto() {
            // Given
            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setNombre("Calzado");
            entityManager.persist(categoria);

            ProductoEntity producto = new ProductoEntity();
            producto.setNombre("Bota Trekking");
            producto.setSku("BOTA-TREK");
            producto.setDescripcion("Desc");
            producto.setPrecio(BigDecimal.valueOf(180));
            producto.setStock(8);
            producto.setActivo(true);
            producto.setCategoriaEntity(categoria);

            entityManager.persist(producto);
            entityManager.flush();

            // When
            Optional<ProductoEntity> resultado = productoRepository.findBySku("BOTA-TREK");

            // Then
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNombre()).isEqualTo("Bota Trekking");
        }

        @Test
        @DisplayName("dado SKU inexistente, cuando se busca, entonces retorna opcional vacío")
        void dadoSkuInexistente_cuandoSeBusca_entoncesRetornaOpcionalVacio() {
            // When
            Optional<ProductoEntity> resultado = productoRepository.findBySku("SKU-FALSO");

            // Then
            assertThat(resultado).isEmpty();
        }
    }
}
