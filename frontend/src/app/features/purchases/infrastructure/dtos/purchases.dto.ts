import { EstadoCompra } from '../../domain/models/estado-compra.enum';
import { ProveedorBasicoResponse, EmpleadoBasicoResponse, CompraDetalleResponse } from '../../domain/models/compra.model';

export interface TipoProveedorResponseDto {
  id: number;
  nombre: string;
}

export interface CrearTipoProveedorRequestDto {
  nombre: string;
}

export interface ActualizarTipoProveedorRequestDto {
  id: number;
  nombre: string;
}

export interface ProveedorResponseDto {
  id: number;
  nombre: string;
  telefono: string;
  email: string;
  direccion: string;
  tipoProveedor: string;
}

export interface CrearProveedorRequestDto {
  nombre: string;
  telefono: string;
  correo: string;
  direccion?: string;
  tipoProveedorId: number;
}

export interface ActualizarProveedorRequestDto {
  id: number;
  nombre: string;
  telefono: string;
  correo: string;
  direccion?: string;
  tipoProveedorId: number;
}

export interface CompraItemRequestDto {
  productoId: number;
  cantidad: number;
  precioUnitario: number;
}

export interface CrearCompraRequestDto {
  proveedorId: number;
  empleadoId: number;
  items: CompraItemRequestDto[];
}

export interface CambiarEstadoCompraRequestDto {
  codigo: EstadoCompra;
}

export interface CompraResponseDto {
  id: number;
  fechaCompra: string;
  estado: { nombre: EstadoCompra };
  total: number;
  proveedor: ProveedorBasicoResponse;
  empleado: EmpleadoBasicoResponse;
  detalles: CompraDetalleResponse[];
}

export interface CompraResumenResponseDto {
  id: number;
  fechaCompra: string;
  estado: EstadoCompra;
  total: number;
  cantidadItem: number;
  proveedor: ProveedorBasicoResponse;
  empleado: EmpleadoBasicoResponse;
}
