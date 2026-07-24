package com.ecommerce.app.sales.integration;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.CategoriaEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.MetodoPagoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.OrdenDetalleEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.OrdenEntity;
import com.ecommerce.app.sales.infrastructure.persistence.repository.OrdenJpaRepository;
import com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("OrdenCheckout Integration Tests")
public class OrdenCheckoutIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrdenJpaRepository ordenRepository;

    private ClienteEntity testCliente;
    private MetodoPagoEntity testMetodoPago;
    private ProductoEntity testProducto;

    @BeforeEach
    void setUp() {
        RolEntity rol = new RolEntity();
        rol.setNombre("Cliente");
        entityManager.persist(rol);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("cliente.checkout@example.com");
        usuario.setNombre("Cliente Checkout");
        usuario.setContrasena("password123");
        usuario.setActivo(true);
        usuario.setDobleFactorActivo(false);
        usuario.setRol(rol);
        entityManager.persist(usuario);

        testCliente = new ClienteEntity();
        testCliente.setUsuario(usuario);
        testCliente.setTelefono("987654321");
        testCliente.setDireccion("Av. Comercio 456");
        entityManager.persist(testCliente);

        testMetodoPago = new MetodoPagoEntity();
        testMetodoPago.setNombre("Tarjeta de Crédito");
        entityManager.persist(testMetodoPago);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Laptops");
        entityManager.persist(categoria);

        testProducto = new ProductoEntity();
        testProducto.setNombre("Laptop Test Checkout");
        testProducto.setSku("LAP-TEST-001");
        testProducto.setPrecio(new BigDecimal("2500.00"));
        testProducto.setStock(10);
        testProducto.setActivo(true);
        testProducto.setCategoria(categoria);
        entityManager.persist(testProducto);

        entityManager.flush();
    }

    @Test
    @DisplayName("cuando se guarda una orden con detalles, entonces se persiste correctamente")
    void cuandoSeGuardaOrdenConDetalles_entoncesSePersisteCorrectamente() {
        // Given
        OrdenEntity orden = new OrdenEntity();
        orden.setCliente(testCliente);
        orden.setMetodoPago(testMetodoPago);
        orden.setDireccionEnvio("Av. Comercio 456");
        orden.setEstadoOrden(EstadoOrdenCodigo.PENDIENTE);
        orden.setFechaOrden(LocalDateTime.now());

        OrdenDetalleEntity detalle = new OrdenDetalleEntity();
        detalle.setOrden(orden);
        detalle.setProducto(testProducto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(new BigDecimal("2500.00"));

        orden.getDetalles().add(detalle);

        // When
        OrdenEntity ordenGuardada = ordenRepository.save(orden);
        entityManager.flush();
        entityManager.clear();

        // Then
        OrdenEntity ordenRecuperada = ordenRepository.findById(ordenGuardada.getId()).orElseThrow();
        assertThat(ordenRecuperada.getCliente().getId()).isEqualTo(testCliente.getId());
        assertThat(ordenRecuperada.getEstadoOrden()).isEqualTo(EstadoOrdenCodigo.PENDIENTE);
        assertThat(ordenRecuperada.getDetalles()).hasSize(1);
        assertThat(ordenRecuperada.getDetalles().get(0).getProducto().getId()).isEqualTo(testProducto.getId());
        assertThat(ordenRecuperada.getDetalles().get(0).getCantidad()).isEqualTo(2);
    }

    @Test
    @DisplayName("cuando se buscan ordenes por cliente, entonces las retorna correctamente")
    void cuandoSeBuscanOrdenesPorCliente_entoncesLasRetorna() {
        // Given
        OrdenEntity orden = new OrdenEntity();
        orden.setCliente(testCliente);
        orden.setMetodoPago(testMetodoPago);
        orden.setDireccionEnvio("Av. Comercio 456");
        orden.setEstadoOrden(EstadoOrdenCodigo.PAGADO);
        orden.setFechaOrden(LocalDateTime.now());
        ordenRepository.save(orden);
        entityManager.flush();

        // When
        List<OrdenEntity> ordenesCliente = ordenRepository.findByClienteOrderByFechaOrdenDesc(testCliente);

        // Then
        assertThat(ordenesCliente).isNotEmpty();
        assertThat(ordenesCliente.get(0).getCliente().getId()).isEqualTo(testCliente.getId());
    }
}
