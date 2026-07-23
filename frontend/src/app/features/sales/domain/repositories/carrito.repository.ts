import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Carrito } from '../models/carrito.model';

export interface AgregarItemCarritoData {
  productoId: number;
  cantidad: number;
}

export interface ActualizarItemCarritoData {
  cantidad: number;
}

export interface CarritoRepository {
  obtenerCarritoActivo(): Observable<Carrito>;
  agregarItem(data: AgregarItemCarritoData): Observable<Carrito>;
  actualizarItem(itemId: number, data: ActualizarItemCarritoData): Observable<Carrito>;
  eliminarItem(itemId: number): Observable<Carrito>;
  vaciarCarrito(): Observable<void>;
}

export const CARRITO_REPOSITORY = new InjectionToken<CarritoRepository>('CarritoRepository');
