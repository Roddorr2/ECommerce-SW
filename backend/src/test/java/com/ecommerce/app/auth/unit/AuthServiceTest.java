package com.ecommerce.app.auth.unit;

import com.ecommerce.app.auth.application.dto.request.*;
import com.ecommerce.app.auth.application.dto.response.AuthResponse;
import com.ecommerce.app.auth.application.dto.response.CambioContrasenaResponse;
import com.ecommerce.app.auth.application.dto.response.SolicitudCambioContrasenaResponse;
import com.ecommerce.app.auth.application.service.AuthService;
import com.ecommerce.app.auth.application.service.CodigoVerificacionService;
import com.ecommerce.app.auth.application.service.EmailService;
import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.RolPort;
import com.ecommerce.app.shared.domain.port.UsuarioPort;
import com.ecommerce.app.infrastructure.security.JwtUtil;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioPort usuarioPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolPort rolPort;

    @Mock
    private ClientePort clientePort;

    @Mock
    private CodigoVerificacionService codigoService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private Rol rolCliente;
    private CodigoVerificacion codigoVerificacion;

    @BeforeEach
    void setUp() {
        rolCliente = new Rol(1, "Cliente");

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Carlos Gómez");
        usuario.setCorreo("carlos@ecommerce.com");
        usuario.setContrasena("encoded-pass");
        usuario.setRol(rolCliente);
        usuario.setActivo(true);

        codigoVerificacion = new CodigoVerificacion(1, usuario, "123456", EstadoCodigoVerificacionCodigo.PENDIENTE, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), 0, 3, null);
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("dado credenciales válidas, cuando inicia sesión, entonces autentica, genera código, envía email y requiere 2FA")
        void dadoCredencialesValidas_cuandoLogin_entoncesGeneraCodigoEnviaEmailYRetornaRequiere2FA() {
            // Given
            AuthRequest request = new AuthRequest("carlos@ecommerce.com", "password");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.generarCodigo(usuario)).thenReturn(codigoVerificacion);

            // When
            AuthResponse resultado = authService.login(request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.requiere2FA()).isTrue();
            assertThat(resultado.mensaje()).contains("Código de verificación enviado a c*****@ecommerce.com");

            verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(usuarioPort).findByCorreo("carlos@ecommerce.com");
            verify(codigoService).generarCodigo(usuario);
            verify(emailService).enviarCodigoVerificacion("carlos@ecommerce.com", "123456");
        }

        @Test
        @DisplayName("dado usuario inexistente en base de datos, cuando inicia sesión, entonces lanza RuntimeException")
        void dadoUsuarioInexistente_cuandoLogin_entoncesLanzaRuntimeException() {
            // Given
            AuthRequest request = new AuthRequest("desconocido@ecommerce.com", "password");
            when(usuarioPort.findByCorreo("desconocido@ecommerce.com")).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado.");

            verify(codigoService, never()).generarCodigo(any());
            verifyNoInteractions(emailService);
        }

        @Test
        @DisplayName("dado error al enviar el email, cuando inicia sesión, entonces lanza RuntimeException de error en envío")
        void dadoErrorEnEnvioEmail_cuandoLogin_entoncesLanzaRuntimeException() {
            // Given
            AuthRequest request = new AuthRequest("carlos@ecommerce.com", "password");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.generarCodigo(usuario)).thenReturn(codigoVerificacion);
            doThrow(new RuntimeException("SMTP Server Down"))
                    .when(emailService).enviarCodigoVerificacion(any(), any());

            // When / Then
            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Error al enviar código de verificación: SMTP Server Down");
        }
    }

    @Nested
    @DisplayName("verificarCodigo")
    class VerificarCodigo {

        @Test
        @DisplayName("dado código de verificación válido, cuando se verifica, entonces genera JWT y completa la autenticación")
        void dadoCodigoValido_cuandoVerificarCodigo_entoncesGeneraTokenYCompletaAutenticacion() {
            // Given
            VerificarCodigoRequest request = new VerificarCodigoRequest("carlos@ecommerce.com", "123456");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.validarCodigo(usuario, "123456")).thenReturn(true);
            when(jwtUtil.generateToken("carlos@ecommerce.com", "Cliente")).thenReturn("mock-jwt-token");

            // When
            AuthResponse resultado = authService.verificarCodigo(request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.token()).isEqualTo("mock-jwt-token");
            assertThat(resultado.rol()).isEqualTo("Cliente");
            assertThat(resultado.nombre()).isEqualTo("Carlos Gómez");
            assertThat(resultado.requiere2FA()).isFalse();

            verify(usuarioPort).findByCorreo("carlos@ecommerce.com");
            verify(codigoService).validarCodigo(usuario, "123456");
            verify(jwtUtil).generateToken("carlos@ecommerce.com", "Cliente");
        }

        @Test
        @DisplayName("dado usuario inexistente, cuando se verifica, entonces lanza RuntimeException")
        void dadoUsuarioInexistente_cuandoVerificarCodigo_entoncesLanzaRuntimeException() {
            // Given
            VerificarCodigoRequest request = new VerificarCodigoRequest("desconocido@ecommerce.com", "123456");
            when(usuarioPort.findByCorreo("desconocido@ecommerce.com")).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> authService.verificarCodigo(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado.");

            verifyNoInteractions(codigoService, jwtUtil);
        }

        @Test
        @DisplayName("dado código inválido o expirado, cuando se verifica, entonces lanza RuntimeException de código inválido")
        void dadoCodigoInvalidoOExpirado_cuandoVerificarCodigo_entoncesLanzaRuntimeException() {
            // Given
            VerificarCodigoRequest request = new VerificarCodigoRequest("carlos@ecommerce.com", "999999");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.validarCodigo(usuario, "999999")).thenReturn(false);

            // When / Then
            assertThatThrownBy(() -> authService.verificarCodigo(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Código inválido o expirado.");

            verifyNoInteractions(jwtUtil);
        }
    }

    @Nested
    @DisplayName("reenviarCodigo")
    class ReenviarCodigo {

        @Test
        @DisplayName("dado usuario existente, cuando se reenvía, entonces genera código nuevo y lo envía por email")
        void dadoUsuarioExistente_cuandoReenviarCodigo_entoncesGeneraYEnviaNuevoCodigo() {
            // Given
            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.generarCodigo(usuario)).thenReturn(codigoVerificacion);

            // When
            AuthResponse resultado = authService.reenviarCodigo("carlos@ecommerce.com");

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.requiere2FA()).isTrue();
            assertThat(resultado.mensaje()).contains("Nuevo código enviado a c*****@ecommerce.com");

            verify(usuarioPort).findByCorreo("carlos@ecommerce.com");
            verify(codigoService).generarCodigo(usuario);
            verify(emailService).enviarCodigoVerificacion("carlos@ecommerce.com", "123456");
        }

        @Test
        @DisplayName("dado usuario inexistente, cuando se reenvía, entonces lanza RuntimeException")
        void dadoUsuarioInexistente_cuandoReenviarCodigo_entoncesLanzaRuntimeException() {
            // Given
            when(usuarioPort.findByCorreo("desconocido@ecommerce.com")).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> authService.reenviarCodigo("desconocido@ecommerce.com"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado.");

            verify(codigoService, never()).generarCodigo(any());
        }
    }

    @Nested
    @DisplayName("registrarCliente")
    class RegistrarCliente {

        @Test
        @DisplayName("dado request válido, cuando se registra cliente, entonces guarda usuario y cliente, y retorna JWT")
        void dadoRequestValido_cuandoRegistrarCliente_entoncesGuardaYRetornaJWT() {
            // Given
            RegistrarClienteRequest request = new RegistrarClienteRequest(
                    "Carlos Gómez", "carlos@ecommerce.com", "password", "987654321", "Calle Mayor 456"
            );

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.empty());
            when(rolPort.findByNombre("Cliente")).thenReturn(Optional.of(rolCliente));
            when(passwordEncoder.encode("password")).thenReturn("encoded-new-pass");
            when(jwtUtil.generateToken("carlos@ecommerce.com", "Cliente")).thenReturn("new-jwt-token");

            ArgumentCaptor<Usuario> captorUsuario = ArgumentCaptor.forClass(Usuario.class);
            ArgumentCaptor<Cliente> captorCliente = ArgumentCaptor.forClass(Cliente.class);

            // When
            AuthResponse resultado = authService.registrarCliente(request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.token()).isEqualTo("new-jwt-token");

            verify(usuarioPort).save(captorUsuario.capture());
            Usuario usuarioGuardado = captorUsuario.getValue();
            assertThat(usuarioGuardado.getNombre()).isEqualTo("Carlos Gómez");
            assertThat(usuarioGuardado.getCorreo()).isEqualTo("carlos@ecommerce.com");
            assertThat(usuarioGuardado.getContrasena()).isEqualTo("encoded-new-pass");
            assertThat(usuarioGuardado.isActivo()).isTrue();
            assertThat(usuarioGuardado.getRol()).isEqualTo(rolCliente);

            verify(clientePort).guardar(captorCliente.capture());
            Cliente clienteGuardado = captorCliente.getValue();
            assertThat(clienteGuardado.getTelefono()).isEqualTo("987654321");
            assertThat(clienteGuardado.getDireccion()).isEqualTo("Calle Mayor 456");
            assertThat(clienteGuardado.getUsuario()).isEqualTo(usuarioGuardado);
        }

        @Test
        @DisplayName("dado email ya existente, cuando se registra, entonces lanza IllegalArgumentException")
        void dadoEmailYaRegistrado_cuandoRegistrarCliente_entoncesLanzaIllegalArgumentException() {
            // Given
            RegistrarClienteRequest request = new RegistrarClienteRequest(
                    "Carlos Gómez", "carlos@ecommerce.com", "password", "987654321", "Calle Mayor 456"
            );
            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));

            // When / Then
            assertThatThrownBy(() -> authService.registrarCliente(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("El email ya está registrado: carlos@ecommerce.com");

            verify(usuarioPort, never()).save(any());
            verify(clientePort, never()).guardar(any());
        }

        @Test
        @DisplayName("dado rol 'Cliente' no existente en base de datos, cuando se registra, entonces lanza IllegalStateException")
        void dadoRolClienteNoConfigurado_cuandoRegistrarCliente_entoncesLanzaIllegalStateException() {
            // Given
            RegistrarClienteRequest request = new RegistrarClienteRequest(
                    "Carlos Gómez", "carlos@ecommerce.com", "password", "987654321", "Calle Mayor 456"
            );
            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.empty());
            when(rolPort.findByNombre("Cliente")).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> authService.registrarCliente(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Rol 'Cliente' no encontrado.");

            verify(usuarioPort, never()).save(any());
            verify(clientePort, never()).guardar(any());
        }
    }

    @Nested
    @DisplayName("solicitarRecuperacion")
    class SolicitarRecuperacion {

        @Test
        @DisplayName("dado correo válido, cuando se solicita recuperación, entonces genera código y envía email")
        void dadoCorreoValido_cuandoSolicitarRecuperacion_entoncesGeneraYEnviaCodigo() {
            // Given
            SolicitarRecuperacionRequest request = new SolicitarRecuperacionRequest("carlos@ecommerce.com");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.generarCodigo(usuario)).thenReturn(codigoVerificacion);

            // When
            SolicitudCambioContrasenaResponse resultado = authService.solicitarRecuperacion(request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.requiere2FA()).isTrue();
            assertThat(resultado.mensaje()).contains("Código enviado a c*****@ecommerce.com");

            verify(usuarioPort).findByCorreo("carlos@ecommerce.com");
            verify(codigoService).generarCodigo(usuario);
            verify(emailService).enviarCodigoVerificacion("carlos@ecommerce.com", "123456");
        }

        @Test
        @DisplayName("dado usuario inexistente, cuando se solicita recuperación, entonces lanza EntityNotFoundException")
        void dadoUsuarioInexistente_cuandoSolicitarRecuperacion_entoncesLanzaEntityNotFoundException() {
            // Given
            SolicitarRecuperacionRequest request = new SolicitarRecuperacionRequest("desconocido@ecommerce.com");
            when(usuarioPort.findByCorreo("desconocido@ecommerce.com")).thenReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> authService.solicitarRecuperacion(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado.");

            verify(codigoService, never()).generarCodigo(any());
        }
    }

    @Nested
    @DisplayName("confirmarRecuperacion")
    class ConfirmarRecuperacion {

        @Test
        @DisplayName("dado código válido y contraseñas coincidentes, cuando se confirma recuperación, entonces actualiza la contraseña en base de datos")
        void dadoRequestValido_cuandoConfirmarRecuperacion_entoncesActualizaContrasena() {
            // Given
            ConfirmarRecuperacionRequest request = new ConfirmarRecuperacionRequest(
                    "carlos@ecommerce.com", "123456", "new-password", "new-password"
            );

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.validarCodigo(usuario, "123456")).thenReturn(true);
            when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-pass");

            // When
            CambioContrasenaResponse resultado = authService.confirmarRecuperacion(request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.exitoso()).isTrue();
            assertThat(resultado.mensaje()).isEqualTo("Contraseña actualizada exitosamente.");

            assertThat(usuario.getContrasena()).isEqualTo("encoded-new-pass");
            verify(usuarioPort).save(usuario);
        }

        @Test
        @DisplayName("dado contraseñas no coincidentes, cuando se confirma recuperación, entonces lanza IllegalArgumentException")
        void dadoContrasenasNoCoincidentes_cuandoConfirmarRecuperacion_entoncesLanzaIllegalArgumentException() {
            // Given
            ConfirmarRecuperacionRequest request = new ConfirmarRecuperacionRequest(
                    "carlos@ecommerce.com", "123456", "new-password", "different-password"
            );

            // When / Then
            assertThatThrownBy(() -> authService.confirmarRecuperacion(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Las contraseñas no coinciden.");

            verify(usuarioPort, never()).save(any());
        }

        @Test
        @DisplayName("dado código inválido o expirado, cuando se confirma recuperación, entonces lanza IllegalArgumentException")
        void dadoCodigoInvalido_cuandoConfirmarRecuperacion_entoncesLanzaIllegalArgumentException() {
            // Given
            ConfirmarRecuperacionRequest request = new ConfirmarRecuperacionRequest(
                    "carlos@ecommerce.com", "999999", "new-password", "new-password"
            );

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(codigoService.validarCodigo(usuario, "999999")).thenReturn(false);

            // When / Then
            assertThatThrownBy(() -> authService.confirmarRecuperacion(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Código inválido o expirado.");

            verify(usuarioPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("cambiarContrasena")
    class CambiarContrasena {

        @Test
        @DisplayName("dado contraseña actual correcta y contraseñas coincidentes, cuando se cambia contraseña, entonces actualiza la contraseña en base de datos")
        void dadoRequestValido_cuandoCambiarContrasena_entoncesActualizaContrasena() {
            // Given
            CambiarContrasenaRequest request = new CambiarContrasenaRequest("current-password", "new-password", "new-password");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("current-password", "encoded-pass")).thenReturn(true);
            when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-pass");

            // When
            CambioContrasenaResponse resultado = authService.cambiarContrasena("carlos@ecommerce.com", request);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.exitoso()).isTrue();
            assertThat(resultado.mensaje()).isEqualTo("Contraseña actualizada exitosamente.");

            assertThat(usuario.getContrasena()).isEqualTo("encoded-new-pass");
            verify(usuarioPort).save(usuario);
        }

        @Test
        @DisplayName("dado nueva contraseña y confirmación no coincidentes, cuando se cambia contraseña, entonces lanza IllegalArgumentException")
        void dadoContrasenasNoCoincidentes_cuandoCambiarContrasena_entoncesLanzaIllegalArgumentException() {
            // Given
            CambiarContrasenaRequest request = new CambiarContrasenaRequest("current-password", "new-password", "different-password");

            // When / Then
            assertThatThrownBy(() -> authService.cambiarContrasena("carlos@ecommerce.com", request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Las contraseñas no coinciden.");

            verify(usuarioPort, never()).save(any());
        }

        @Test
        @DisplayName("dado contraseña actual incorrecta, cuando se cambia contraseña, entonces lanza IllegalArgumentException")
        void dadoContrasenaActualIncorrecta_cuandoCambiarContrasena_entoncesLanzaIllegalArgumentException() {
            // Given
            CambiarContrasenaRequest request = new CambiarContrasenaRequest("wrong-password", "new-password", "new-password");

            when(usuarioPort.findByCorreo("carlos@ecommerce.com")).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("wrong-password", "encoded-pass")).thenReturn(false);

            // When / Then
            assertThatThrownBy(() -> authService.cambiarContrasena("carlos@ecommerce.com", request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("La contraseña actual es incorrecta.");

            verify(usuarioPort, never()).save(any());
        }
    }
}
