export interface AreaResponseDto {
  id: number;
  nombre: string;
}

export interface CrearAreaRequestDto {
  nombre: string;
}

export interface ActualizarAreaRequestDto {
  id: number;
  nombre: string;
}

export interface CargoResponseDto {
  id: number;
  nombre: string;
}

export interface CrearCargoRequestDto {
  nombre: string;
}

export interface ActualizarCargoRequestDto {
  id: number;
  nombre: string;
}

export interface EmpleadoResponseDto {
  id: number;
  usuarioId: number;
  nombreUsuario: string;
  emailUsuario: string;
  area: string;
  cargo: string;
}

export interface CrearEmpleadoRequestDto {
  usuarioId: number;
  areaId: number;
  cargoId: number;
}

export interface ActualizarEmpleadoRequestDto {
  id: number;
  usuarioId: number;
  areaId: number;
  cargoId: number;
}

export interface RolResponseDto {
  id: number;
  nombre: string;
}

export interface CrearRolRequestDto {
  nombre: string;
}

export interface ActualizarRolRequestDto {
  id: number;
  nombre: string;
}

export interface UsuarioResponseDto {
  id: number;
  nombre: string;
  email: string;
  estado: boolean;
  rol: string;
  dobleFactorActivo: boolean;
}

export interface CrearUsuarioRequestDto {
  nombre: string;
  correo: string;
  contrasena: string;
  activo: boolean;
  rolId: number;
}

export interface ActualizarUsuarioRequestDto {
  id: number;
  nombre: string;
  correo: string;
  activo: boolean;
  rolId: number;
}
