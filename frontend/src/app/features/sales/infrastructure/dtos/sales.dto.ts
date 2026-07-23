import { EstadoOrden } from '../../domain/models/estado-orden.enum';
import { ClienteBasicoResponse, MetodoPagoResponse, OrdenDetalleResponse } from '../../domain/models/orden.model';

export interface ClienteResponseDto {
  id: number;
  usuarioId: number;
  usuario: string;
  correo: string;
  telefono: string;
  direccion: string;
}

export interface CrearClienteRequestDto {
  usuarioId: number;
  correo: string;
  telefono: string;
  direccion: string;
}

export interface ActualizarClienteRequestDto {
  id: number;
  usuarioId: number;
  correo: string;
  telefono: string;
  direccion: string;
}

export interface MetodoPagoResponseDto {
  id: number;
  nombre: string;
}

export interface CrearMetodoPagoRequestDto {
  nombre: string;
}

export interface ActualizarMetodoPagoRequestDto {
  id: number;
  nombre: string;
}

export interface CarritoItemResponseDto {
  id: number;
  productoId: number;
  productoNombre: string;
  productoSku: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
  imagenNombre: string;
}

export interface CarritoResponseDto {
  id: number;
  items: CarritoItemResponseDto[];
  total: number;
}

export interface AgregarItemCarritoRequestDto {
  productoId: number;
  cantidad: number;
}

export interface ActualizarItemCarritoRequestDto {
  cantidad: number;
}

export interface OrdenResponseDto {
  id: number;
  fechaOrden: string;
  direccionEnvio: string | null;
  total: number;
  estado: { codigo: EstadoOrden };
  cliente: ClienteBasicoResponse;
  metodoPago: MetodoPagoResponse;
  detalles: OrdenDetalleResponse[];
}

export interface OrdenResumenResponseDto {
  id: number;
  fechaOrden: string;
  estado: EstadoOrden;
  total: number;
  cantidadItems: number;
  cliente: ClienteBasicoResponse;
}

export interface CrearOrdenDesdeCarritoRequestDto {
  metodoPagoId: number;
}

export interface CambiarEstadoOrdenRequestDto {
  codigo: EstadoOrden;
}
