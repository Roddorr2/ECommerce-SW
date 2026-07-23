import { TipoProveedor } from '../../domain/models/tipo-proveedor.model';
import { Proveedor } from '../../domain/models/proveedor.model';
import { Compra, CompraResumen } from '../../domain/models/compra.model';

import {
  TipoProveedorResponseDto,
  CrearTipoProveedorRequestDto,
  ActualizarTipoProveedorRequestDto,
  ProveedorResponseDto,
  CrearProveedorRequestDto,
  ActualizarProveedorRequestDto,
  CompraItemRequestDto,
  CrearCompraRequestDto,
  CambiarEstadoCompraRequestDto,
  CompraResponseDto,
  CompraResumenResponseDto
} from '../dtos/purchases.dto';

import { CrearTipoProveedorData, ActualizarTipoProveedorData } from '../../domain/repositories/tipo-proveedor.repository';
import { CrearProveedorData, ActualizarProveedorData } from '../../domain/repositories/proveedor.repository';
import { CrearCompraData, CambiarEstadoCompraData } from '../../domain/repositories/compra.repository';

export class PurchasesMapper {
  // TipoProveedor
  public static toTipoProveedorDomain(dto: TipoProveedorResponseDto): TipoProveedor {
    return new TipoProveedor(dto.id, dto.nombre);
  }

  public static toTipoProveedorDomainList(dtos: TipoProveedorResponseDto[]): TipoProveedor[] {
    return dtos.map(this.toTipoProveedorDomain);
  }

  public static toCrearTipoProveedorRequest(data: CrearTipoProveedorData): CrearTipoProveedorRequestDto {
    return { nombre: data.nombre };
  }

  public static toActualizarTipoProveedorRequest(data: ActualizarTipoProveedorData): ActualizarTipoProveedorRequestDto {
    return { id: data.id, nombre: data.nombre };
  }

  // Proveedor
  public static toProveedorDomain(dto: ProveedorResponseDto): Proveedor {
    return new Proveedor(dto.id, dto.nombre, dto.telefono, dto.email, dto.direccion, dto.tipoProveedor);
  }

  public static toProveedorDomainList(dtos: ProveedorResponseDto[]): Proveedor[] {
    return dtos.map(this.toProveedorDomain);
  }

  public static toCrearProveedorRequest(data: CrearProveedorData): CrearProveedorRequestDto {
    return {
      nombre: data.nombre,
      telefono: data.telefono,
      correo: data.correo,
      direccion: data.direccion,
      tipoProveedorId: data.tipoProveedorId
    };
  }

  public static toActualizarProveedorRequest(data: ActualizarProveedorData): ActualizarProveedorRequestDto {
    return {
      id: data.id,
      nombre: data.nombre,
      telefono: data.telefono,
      correo: data.correo,
      direccion: data.direccion,
      tipoProveedorId: data.tipoProveedorId
    };
  }

  // Compra
  public static toCompraDomain(dto: CompraResponseDto): Compra {
    return new Compra(dto.id, dto.fechaCompra, dto.estado, dto.total, dto.proveedor, dto.empleado, dto.detalles);
  }

  public static toCompraResumenDomain(dto: CompraResumenResponseDto): CompraResumen {
    return new CompraResumen(dto.id, dto.fechaCompra, dto.estado, dto.total, dto.cantidadItem, dto.proveedor, dto.empleado);
  }

  public static toCompraResumenDomainList(dtos: CompraResumenResponseDto[]): CompraResumen[] {
    return dtos.map(this.toCompraResumenDomain);
  }

  public static toCrearCompraRequest(data: CrearCompraData): CrearCompraRequestDto {
    return {
      proveedorId: data.proveedorId,
      empleadoId: data.empleadoId,
      items: data.items.map(item => ({
        productoId: item.productoId,
        cantidad: item.cantidad,
        precioUnitario: item.precioUnitario
      }))
    };
  }

  public static toCambiarEstadoRequest(data: CambiarEstadoCompraData): CambiarEstadoCompraRequestDto {
    return { codigo: data.estado };
  }
}
