package com.ecommerce.app.auth.integration;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.infrastructure.persistence.entity.CodigoVerificacionEntity;
import com.ecommerce.app.auth.infrastructure.persistence.repository.CodigoVerificacionJpaRepository;
import com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CodigoVerificacion Repository Integration Tests")
public class CodigoVerificacionIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CodigoVerificacionJpaRepository codigoRepository;

    private UsuarioEntity testUsuario;

    @BeforeEach
    void setUp() {
        RolEntity rol = new RolEntity();
        rol.setNombre("Cliente");
        entityManager.persist(rol);

        testUsuario = new UsuarioEntity();
        testUsuario.setCorreo("test.otp@example.com");
        testUsuario.setNombre("Test User OTP");
        testUsuario.setContrasena("encoded_password");
        testUsuario.setActivo(true);
        testUsuario.setDobleFactorActivo(true);
        testUsuario.setRol(rol);
        entityManager.persist(testUsuario);
        entityManager.flush();
    }

    @Nested
    @DisplayName("findByUsuarioAndCodigoAndEstado")
    class FindByUsuarioAndCodigoAndEstado {

        @Test
        @DisplayName("cuando existe codigo activo para el usuario, entonces lo retorna")
        void cuandoExisteCodigoActivo_entoncesLoRetorna() {
            CodigoVerificacionEntity codigoEntity = new CodigoVerificacionEntity();
            codigoEntity.setUsuario(testUsuario);
            codigoEntity.setCodigo("123456");
            codigoEntity.setEstado(EstadoCodigoVerificacionCodigo.PENDIENTE);
            codigoEntity.setFechaGeneracion(LocalDateTime.now());
            codigoEntity.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));
            codigoEntity.setIntentosRealizados(0);
            codigoEntity.setIntentosMaximos(3);
            entityManager.persist(codigoEntity);
            entityManager.flush();

            Optional<CodigoVerificacionEntity> resultado = codigoRepository
                    .findByUsuarioAndCodigoAndEstado(testUsuario, "123456", EstadoCodigoVerificacionCodigo.PENDIENTE);

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getCodigo()).isEqualTo("123456");
            assertThat(resultado.get().getUsuario().getCorreo()).isEqualTo("test.otp@example.com");
        }

        @Test
        @DisplayName("cuando el codigo ingresado no coincide, entonces retorna Optional vacio")
        void cuandoCodigoNoCoincide_entoncesRetornaVacio() {
            CodigoVerificacionEntity codigoEntity = new CodigoVerificacionEntity();
            codigoEntity.setUsuario(testUsuario);
            codigoEntity.setCodigo("123456");
            codigoEntity.setEstado(EstadoCodigoVerificacionCodigo.PENDIENTE);
            codigoEntity.setFechaGeneracion(LocalDateTime.now());
            codigoEntity.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));
            entityManager.persist(codigoEntity);
            entityManager.flush();

            Optional<CodigoVerificacionEntity> resultado = codigoRepository
                    .findByUsuarioAndCodigoAndEstado(testUsuario, "999999", EstadoCodigoVerificacionCodigo.PENDIENTE);

            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("findCodigosExpirados")
    class FindCodigosExpirados {

        @Test
        @DisplayName("cuando existen codigos pendientes con fecha de expiracion anterior a la fecha actual, entonces los retorna")
        void cuandoCodigosExpirados_entoncesLosRetorna() {
            CodigoVerificacionEntity codigoExpirado = new CodigoVerificacionEntity();
            codigoExpirado.setUsuario(testUsuario);
            codigoExpirado.setCodigo("654321");
            codigoExpirado.setEstado(EstadoCodigoVerificacionCodigo.PENDIENTE);
            codigoExpirado.setFechaGeneracion(LocalDateTime.now().minusMinutes(30));
            codigoExpirado.setFechaExpiracion(LocalDateTime.now().minusMinutes(20));
            entityManager.persist(codigoExpirado);

            CodigoVerificacionEntity codigoVigente = new CodigoVerificacionEntity();
            codigoVigente.setUsuario(testUsuario);
            codigoVigente.setCodigo("111222");
            codigoVigente.setEstado(EstadoCodigoVerificacionCodigo.PENDIENTE);
            codigoVigente.setFechaGeneracion(LocalDateTime.now());
            codigoVigente.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));
            entityManager.persist(codigoVigente);
            entityManager.flush();

            List<CodigoVerificacionEntity> expirados = codigoRepository
                    .findCodigosExpirados(EstadoCodigoVerificacionCodigo.PENDIENTE, LocalDateTime.now());

            assertThat(expirados).hasSize(1);
            assertThat(expirados.get(0).getCodigo()).isEqualTo("654321");
        }
    }
}
