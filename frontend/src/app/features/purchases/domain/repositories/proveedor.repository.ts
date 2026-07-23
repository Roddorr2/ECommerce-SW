import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Proveedor } from '../models/proveedor.model';

export interface CrearProveedorData {
  nombre: string;
  telefono: string;
  correo: string;
  direccion: string;
  tipoProveedorId: number;
}

export interface ActualizarProveedorData {
  id: number;
  nombre: string;
  telefono: string;
  correo: string;
  direccion: string;
  tipoProveedorId: number;
}

export interface ProveedorRepository {
  listarProveedores(): Observable<Proveedor[]>;
  obtenerPorId(id: number): Observable<Proveedor>;
  registrarProveedor(data: CrearProveedorData): Observable<Proveedor>;
  editarProveedor(data: ActualizarProveedorData): Observable<Proveedor>;
  eliminarProveedor(id: number): Observable<void>;
  buscarPorTipoProveedor(tipoProveedor: string): Observable<Proveedor[]>;
}

export const PROVEEDOR_REPOSITORY = new InjectionToken<ProveedorRepository>('ProveedorRepository');
