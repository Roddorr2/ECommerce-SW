import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Compra, CompraResumen } from '../models/compra.model';
import { EstadoCompra } from '../models/estado-compra.enum';

export interface CrearCompraItemData {
  productoId: number;
  cantidad: number;
  precioUnitario: number;
}

export interface CrearCompraData {
  proveedorId: number;
  empleadoId: number;
  items: CrearCompraItemData[];
}

export interface CambiarEstadoCompraData {
  estado: EstadoCompra;
}

export interface CompraRepository {
  listarCompras(): Observable<CompraResumen[]>;
  obtenerPorId(id: number): Observable<Compra>;
  crearCompra(data: CrearCompraData): Observable<Compra>;
  recibirCompra(compraId: number): Observable<Compra>;
  cancelarCompra(compraId: number, motivo?: string): Observable<Compra>;
  cambiarEstado(compraId: number, data: CambiarEstadoCompraData): Observable<Compra>;
  obtenerPorProveedor(proveedorId: number): Observable<CompraResumen[]>;
  obtenerPorEstado(estado: EstadoCompra): Observable<CompraResumen[]>;
  eliminarCompra(id: number): Observable<void>;
}

export const COMPRA_REPOSITORY = new InjectionToken<CompraRepository>('CompraRepository');
