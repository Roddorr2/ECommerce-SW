package com.ecommerce.app.auth.application.service;

import com.ecommerce.app.auth.application.dto.request.*;
import com.ecommerce.app.auth.application.dto.response.AuthResponse;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.auth.application.dto.response.CambioContrasenaResponse;
import com.ecommerce.app.auth.application.dto.response.SolicitudCambioContrasenaResponse;
import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.sales.domain.port.ClientePort;
import com.ecommerce.app.shared.domain.port.RolPort;
import com.ecommerce.app.shared.domain.port.UsuarioPort;
import com.ecommerce.app.infrastructure.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioPort usuarioPort;
    private final PasswordEncoder passwordEncoder;
    private final RolPort rolPort;
    private final ClientePort clientePort;
    private final CodigoVerificacionService codigoService;
    public final EmailService emailService;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioPort usuarioPort, PasswordEncoder passwordEncoder, RolPort rolPort, ClientePort clientePort, CodigoVerificacionService codigoService, EmailService emailService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioPort = usuarioPort;
        this.passwordEncoder = passwordEncoder;
        this.rolPort = rolPort;
        this.clientePort = clientePort;
        this.codigoService = codigoService;
        this.emailService = emailService;
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.correo(),
                        request.contrasena()
                )
        );

        Usuario usuario = usuarioPort.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!usuario.requiere2FA()) {
            String token = jwtUtil.generateToken(
                    usuario.getCorreo(),
                    usuario.getRol().getNombre()
            );
            return AuthResponse.conToken(
                    token,
                    usuario.getRol().getNombre(),
                    usuario.getNombre()
            );
        }

        CodigoVerificacion codigo = codigoService.generarCodigo(usuario);

        try {
            emailService.enviarCodigoVerificacion(usuario.getCorreo(), codigo.getCodigo());
        } catch (Exception ex) {
            throw new RuntimeException("Error al enviar código de verificación: " + ex.getMessage());
        }

        return AuthResponse.requiere2FA(
                "Código de verificación enviado a " + ocultarEmail(usuario.getCorreo()) +
                        ". El código expira en 10 minutos."
        );
    }

    @Transactional
    public AuthResponse verificarCodigo(VerificarCodigoRequest request) {
        Usuario usuario = usuarioPort.findByCorreo(request.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        boolean codigoValido = codigoService.validarCodigo(usuario, request.codigo());

        if (!codigoValido) {
            throw new RuntimeException("Código inválido o expirado.");
        }

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().getNombre()
        );

        return AuthResponse.conToken(
                token,
                usuario.getRol().getNombre(),
                usuario.getNombre()
        );
    }

    @Transactional
    public AuthResponse reenviarCodigo(String correo) {
        Usuario usuario = usuarioPort.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        CodigoVerificacion codigo = codigoService.generarCodigo(usuario);

        try {
            emailService.enviarCodigoVerificacion(usuario.getCorreo(), codigo.getCodigo());
        } catch (Exception e) {
            throw new RuntimeException("Error al reenviar código: " + e.getMessage());
        }

        return AuthResponse.requiere2FA(
                "Nuevo código enviado a " + ocultarEmail(usuario.getCorreo()) +
                        ". El código expira en 10 minutos."
        );
    }

    @Transactional
    public AuthResponse registrarCliente(RegistrarClienteRequest request) {
        if (usuarioPort.findByCorreo(request.correo()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.correo());
        }

        Rol rolCliente = rolPort.findByNombre("Cliente")
                .orElseThrow(() -> new IllegalStateException("Rol 'Cliente' no encontrado."));

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.nombre());
        nuevo.setCorreo(request.correo());
        nuevo.setContrasena(passwordEncoder.encode(request.contrasena()));
        nuevo.setActivo(true);
        nuevo.setRol(rolCliente);

        Usuario guardado = usuarioPort.save(nuevo);

        Cliente cliente = new Cliente();
        cliente.setTelefono(request.telefono());
        cliente.setDireccion(request.direccion());
        cliente.setUsuario(guardado);

        clientePort.guardar(cliente);

        String token = jwtUtil.generateToken(
                nuevo.getCorreo(),
                nuevo.getRol().getNombre()
        );

        return AuthResponse.conToken(
                token,
                nuevo.getRol().getNombre(),
                nuevo.getNombre()
        );
    }

    @Transactional
    public SolicitudCambioContrasenaResponse solicitarRecuperacion(SolicitarRecuperacionRequest request) {
        Usuario usuario = usuarioPort.findByCorreo(request.correo())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        CodigoVerificacion codigo = codigoService.generarCodigo(usuario);

        try {
            emailService.enviarCodigoVerificacion(usuario.getCorreo(), codigo.getCodigo());
        } catch (Exception ex) {
            throw new RuntimeException("Error al enviar código: " + ex.getMessage());
        }

        return new SolicitudCambioContrasenaResponse(
                "Código enviado a " + ocultarEmail(usuario.getCorreo()),
                true
        );
    }

    @Transactional
    public CambioContrasenaResponse confirmarRecuperacion(ConfirmarRecuperacionRequest request) {
        if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        Usuario usuario = usuarioPort.findByCorreo(request.correo())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        boolean codigoValido = codigoService.validarCodigo(usuario, request.codigo());
        if (!codigoValido) {
            throw new IllegalArgumentException("Código inválido o expirado.");
        }

        usuario.setContrasena(passwordEncoder.encode(request.nuevaContrasena()));
        usuarioPort.save(usuario);

        return new CambioContrasenaResponse("Contraseña actualizada exitosamente.", true);
    }

    @Transactional
    public CambioContrasenaResponse cambiarContrasena(String correo, CambiarContrasenaRequest request) {
        if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        Usuario usuario = usuarioPort.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        if (!passwordEncoder.matches(request.contrasenaActual(), usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        usuario.setContrasena(passwordEncoder.encode(request.nuevaContrasena()));
        usuarioPort.save(usuario);

        return new CambioContrasenaResponse("Contraseña actualizada exitosamente.", true);
    }

    private String ocultarEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }

        String[] partes = email.split("@");
        String usuario = partes[0];
        String dominio = partes[1];

        if (usuario.length() <= 2) {
            return usuario.charAt(0) + "*@" + dominio;
        }

        String usuarioOculto = usuario.charAt(0) + "*".repeat(usuario.length() - 1);
        return usuarioOculto + "@" + dominio;
    }
}
