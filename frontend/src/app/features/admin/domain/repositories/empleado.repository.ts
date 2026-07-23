import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Empleado } from '../models/empleado.model';

export interface EmpleadoRepository {
  listarEmpleados(): Observable<Empleado[]>;
  obtenerPorId(id: number): Observable<Empleado>;
  registrarEmpleado(usuarioId: number, areaId: number, cargoId: number): Observable<Empleado>;
  editarEmpleado(id: number, usuarioId: number, areaId: number, cargoId: number): Observable<Empleado>;
  eliminarEmpleado(id: number): Observable<void>;
}

export const EMPLEADO_REPOSITORY = new InjectionToken<EmpleadoRepository>('EmpleadoRepository');
