import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Area } from '../models/area.model';

export interface AreaRepository {
  listarAreas(): Observable<Area[]>;
  obtenerPorId(id: number): Observable<Area>;
  registrarArea(nombre: string): Observable<Area>;
  editarArea(id: number, nombre: string): Observable<Area>;
  eliminarArea(id: number): Observable<void>;
}

export const AREA_REPOSITORY = new InjectionToken<AreaRepository>('AreaRepository');
