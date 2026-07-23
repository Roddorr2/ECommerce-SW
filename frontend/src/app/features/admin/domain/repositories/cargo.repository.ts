import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Cargo } from '../models/cargo.model';

export interface CargoRepository {
  listarCargos(): Observable<Cargo[]>;
  obtenerPorId(id: number): Observable<Cargo>;
  registrarCargo(nombre: string): Observable<Cargo>;
  editarCargo(id: number, nombre: string): Observable<Cargo>;
  eliminarCargo(id: number): Observable<void>;
}

export const CARGO_REPOSITORY = new InjectionToken<CargoRepository>('CargoRepository');
