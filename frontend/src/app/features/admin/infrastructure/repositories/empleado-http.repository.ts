import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Empleado } from '../../domain/models/empleado.model';
import { EmpleadoRepository } from '../../domain/repositories/empleado.repository';
import { EmpleadoResponseDto } from '../dtos/admin.dto';
import { AdminMapper } from '../mappers/admin.mapper';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoHttpRepository implements EmpleadoRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/empleados`;

  listarEmpleados(): Observable<Empleado[]> {
    return this.http.get<EmpleadoResponseDto[]>(this.apiUrl).pipe(
      map(dtos => dtos.map(AdminMapper.toEmpleado))
    );
  }

  obtenerPorId(id: number): Observable<Empleado> {
    return this.http.get<EmpleadoResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map(AdminMapper.toEmpleado)
    );
  }

  registrarEmpleado(usuarioId: number, areaId: number, cargoId: number): Observable<Empleado> {
    return this.http.post<EmpleadoResponseDto>(this.apiUrl, { usuarioId, areaId, cargoId }).pipe(
      map(AdminMapper.toEmpleado)
    );
  }

  editarEmpleado(id: number, usuarioId: number, areaId: number, cargoId: number): Observable<Empleado> {
    return this.http.put<EmpleadoResponseDto>(this.apiUrl, { id, usuarioId, areaId, cargoId }).pipe(
      map(AdminMapper.toEmpleado)
    );
  }

  eliminarEmpleado(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
