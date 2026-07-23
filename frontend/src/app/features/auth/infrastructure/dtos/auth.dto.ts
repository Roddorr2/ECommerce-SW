export interface AuthRequestDto {
  correo: string;
  contrasena: string;
}

export interface AuthResponseDto {
  token: string | null;
  rol: string | null;
  nombre: string | null;
  requiere2FA: boolean;
  mensaje: string;
}

export interface RegistrarClienteRequestDto {
  nombre: string;
  telefono: string;
  correo: string;
  contrasena: string;
  direccion: string;
}

export interface VerificarCodigoRequestDto {
  correo: string;
  codigo: string;
}

export interface ReenviarCodigoRequestDto {
  correo: string;
}

export interface SolicitarRecuperacionRequestDto {
  correo: string;
}

export interface SolicitudCambioContrasenaResponseDto {
  mensaje: string;
}

export interface ConfirmarRecuperacionRequestDto {
  correo: string;
  codigo: string;
  nuevaContrasena: string;
}

export interface CambioContrasenaResponseDto {
  mensaje: string;
}

export interface CambiarContrasenaRequestDto {
  contrasenaActual: string;
  nuevaContrasena: string;
}
