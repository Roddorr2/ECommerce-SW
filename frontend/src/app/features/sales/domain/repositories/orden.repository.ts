import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Orden, OrdenResumen } from '../models/orden.model';
import { EstadoOrden } from '../models/estado-orden.enum';

export interface CrearOrdenDesdeCarritoData {
  metodoPagoId: number;
}

export interface CambiarEstadoOrdenData {
  estado: EstadoOrden;
}

export interface OrdenRepository {
  crearOrdenDesdeCarrito(data: CrearOrdenDesdeCarritoData): Observable<Orden>;
  obtenerPorId(ordenId: number): Observable<Orden>;
  obtenerMisOrdenes(): Observable<OrdenResumen[]>;
  listarOrdenes(): Observable<OrdenResumen[]>;
  obtenerPorCliente(clienteId: number): Observable<OrdenResumen[]>;
  obtenerPorEstado(estado: EstadoOrden): Observable<OrdenResumen[]>;
  cambiarEstado(ordenId: number, data: CambiarEstadoOrdenData): Observable<Orden>;
  cancelarOrden(ordenId: number, motivo?: string): Observable<Orden>;
}

export const ORDEN_REPOSITORY = new InjectionToken<OrdenRepository>('OrdenRepository');
