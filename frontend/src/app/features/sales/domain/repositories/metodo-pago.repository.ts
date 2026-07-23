import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { MetodoPago } from '../models/metodo-pago.model';

export interface CrearMetodoPagoData {
  nombre: string;
}

export interface ActualizarMetodoPagoData {
  id: number;
  nombre: string;
}

export interface MetodoPagoRepository {
  listarMetodosPago(): Observable<MetodoPago[]>;
  obtenerPorId(id: number): Observable<MetodoPago>;
  registrarMetodoPago(data: CrearMetodoPagoData): Observable<MetodoPago>;
  editarMetodoPago(data: ActualizarMetodoPagoData): Observable<MetodoPago>;
  eliminarMetodoPago(id: number): Observable<void>;
  buscarPorNombre(nombre: string): Observable<MetodoPago[]>;
}

export const METODO_PAGO_REPOSITORY = new InjectionToken<MetodoPagoRepository>('MetodoPagoRepository');
