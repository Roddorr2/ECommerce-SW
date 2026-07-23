import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Area } from '../../domain/models/area.model';
import { AreaRepository } from '../../domain/repositories/area.repository';
import { AreaResponseDto } from '../dtos/admin.dto';
import { AdminMapper } from '../mappers/admin.mapper';

@Injectable({
  providedIn: 'root'
})
export class AreaHttpRepository implements AreaRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/areas`;

  listarAreas(): Observable<Area[]> {
    return this.http.get<AreaResponseDto[]>(this.apiUrl).pipe(
      map(dtos => dtos.map(AdminMapper.toArea))
    );
  }

  obtenerPorId(id: number): Observable<Area> {
    return this.http.get<AreaResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map(AdminMapper.toArea)
    );
  }

  registrarArea(nombre: string): Observable<Area> {
    return this.http.post<AreaResponseDto>(this.apiUrl, { nombre }).pipe(
      map(AdminMapper.toArea)
    );
  }

  editarArea(id: number, nombre: string): Observable<Area> {
    return this.http.put<AreaResponseDto>(this.apiUrl, { id, nombre }).pipe(
      map(AdminMapper.toArea)
    );
  }

  eliminarArea(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
