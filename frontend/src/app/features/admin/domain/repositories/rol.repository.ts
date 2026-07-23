import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Rol } from '../models/rol.model';

export interface RolRepository {
  listarRoles(): Observable<Rol[]>;
  obtenerPorId(id: number): Observable<Rol>;
  registrarRol(nombre: string): Observable<Rol>;
  editarRol(id: number, nombre: string): Observable<Rol>;
  eliminarRol(id: number): Observable<void>;
  buscarPorNombre(nombre: string): Observable<Rol[]>;
}

export const ROL_REPOSITORY = new InjectionToken<RolRepository>('RolRepository');
