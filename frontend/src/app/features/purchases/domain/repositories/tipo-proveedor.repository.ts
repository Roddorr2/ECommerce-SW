import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoProveedor } from '../models/tipo-proveedor.model';

export interface CrearTipoProveedorData {
  nombre: string;
}

export interface ActualizarTipoProveedorData {
  id: number;
  nombre: string;
}

export interface TipoProveedorRepository {
  listarTiposProveedor(): Observable<TipoProveedor[]>;
  obtenerPorId(id: number): Observable<TipoProveedor>;
  registrarTipoProveedor(data: CrearTipoProveedorData): Observable<TipoProveedor>;
  editarTipoProveedor(data: ActualizarTipoProveedorData): Observable<TipoProveedor>;
  eliminarTipoProveedor(id: number): Observable<void>;
  buscarPorNombre(nombre: string): Observable<TipoProveedor[]>;
}

export const TIPO_PROVEEDOR_REPOSITORY = new InjectionToken<TipoProveedorRepository>('TipoProveedorRepository');
