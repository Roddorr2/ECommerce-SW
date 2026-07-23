import { Cliente } from '../../domain/models/cliente.model';
import { MetodoPago } from '../../domain/models/metodo-pago.model';
import { Carrito, CarritoItem } from '../../domain/models/carrito.model';
import { Orden, OrdenResumen } from '../../domain/models/orden.model';

import {
  ClienteResponseDto,
  CrearClienteRequestDto,
  ActualizarClienteRequestDto,
  MetodoPagoResponseDto,
  CrearMetodoPagoRequestDto,
  ActualizarMetodoPagoRequestDto,
  CarritoResponseDto,
  CarritoItemResponseDto,
  AgregarItemCarritoRequestDto,
  ActualizarItemCarritoRequestDto,
  OrdenResponseDto,
  OrdenResumenResponseDto,
  CrearOrdenDesdeCarritoRequestDto,
  CambiarEstadoOrdenRequestDto
} from '../dtos/sales.dto';

import { RegistrarClienteData, EditarClienteData } from '../../domain/repositories/cliente.repository';
import { CrearMetodoPagoData, ActualizarMetodoPagoData } from '../../domain/repositories/metodo-pago.repository';
import { AgregarItemCarritoData, ActualizarItemCarritoData } from '../../domain/repositories/carrito.repository';
import { CrearOrdenDesdeCarritoData, CambiarEstadoOrdenData } from '../../domain/repositories/orden.repository';

export class SalesMapper {
  // Cliente
  public static toClienteDomain(dto: ClienteResponseDto): Cliente {
    return new Cliente(dto.id, dto.usuarioId, dto.usuario, dto.correo, dto.telefono, dto.direccion);
  }

  public static toClienteDomainList(dtos: ClienteResponseDto[]): Cliente[] {
    return dtos.map(this.toClienteDomain);
  }

  public static toCrearClienteRequest(data: RegistrarClienteData): CrearClienteRequestDto {
    return { usuarioId: data.usuarioId, correo: data.correo, telefono: data.telefono, direccion: data.direccion };
  }

  public static toActualizarClienteRequest(data: EditarClienteData): ActualizarClienteRequestDto {
    return { id: data.id, usuarioId: data.usuarioId, correo: data.correo, telefono: data.telefono, direccion: data.direccion };
  }

  // MetodoPago
  public static toMetodoPagoDomain(dto: MetodoPagoResponseDto): MetodoPago {
    return new MetodoPago(dto.id, dto.nombre);
  }

  public static toMetodoPagoDomainList(dtos: MetodoPagoResponseDto[]): MetodoPago[] {
    return dtos.map(this.toMetodoPagoDomain);
  }

  public static toCrearMetodoPagoRequest(data: CrearMetodoPagoData): CrearMetodoPagoRequestDto {
    return { nombre: data.nombre };
  }

  public static toActualizarMetodoPagoRequest(data: ActualizarMetodoPagoData): ActualizarMetodoPagoRequestDto {
    return { id: data.id, nombre: data.nombre };
  }

  // Carrito
  public static toCarritoItemDomain(dto: CarritoItemResponseDto): CarritoItem {
    return {
      id: dto.id,
      productoId: dto.productoId,
      productoNombre: dto.productoNombre,
      productoSku: dto.productoSku,
      precioUnitario: dto.precioUnitario,
      cantidad: dto.cantidad,
      subtotal: dto.subtotal,
      imagenNombre: dto.imagenNombre
    };
  }

  public static toCarritoDomain(dto: CarritoResponseDto): Carrito {
    return new Carrito(dto.id, dto.items.map(this.toCarritoItemDomain), dto.total);
  }

  public static toAgregarItemRequest(data: AgregarItemCarritoData): AgregarItemCarritoRequestDto {
    return { productoId: data.productoId, cantidad: data.cantidad };
  }

  public static toActualizarItemRequest(data: ActualizarItemCarritoData): ActualizarItemCarritoRequestDto {
    return { cantidad: data.cantidad };
  }

  // Orden
  public static toOrdenDomain(dto: OrdenResponseDto): Orden {
    return new Orden(dto.id, dto.fechaOrden, dto.direccionEnvio, dto.total, dto.estado, dto.cliente, dto.metodoPago, dto.detalles);
  }

  public static toOrdenResumenDomain(dto: OrdenResumenResponseDto): OrdenResumen {
    return new OrdenResumen(dto.id, dto.fechaOrden, dto.estado, dto.total, dto.cantidadItems, dto.cliente);
  }

  public static toOrdenResumenDomainList(dtos: OrdenResumenResponseDto[]): OrdenResumen[] {
    return dtos.map(this.toOrdenResumenDomain);
  }

  public static toCrearOrdenRequest(data: CrearOrdenDesdeCarritoData): CrearOrdenDesdeCarritoRequestDto {
    return { metodoPagoId: data.metodoPagoId };
  }

  public static toCambiarEstadoRequest(data: CambiarEstadoOrdenData): CambiarEstadoOrdenRequestDto {
    return { codigo: data.estado };
  }
}
