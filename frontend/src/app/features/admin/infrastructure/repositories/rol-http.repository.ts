import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Rol } from '../../domain/models/rol.model';
import { RolRepository } from '../../domain/repositories/rol.repository';
import { RolResponseDto } from '../dtos/admin.dto';
import { AdminMapper } from '../mappers/admin.mapper';

@Injectable({
  providedIn: 'root'
})
export class RolHttpRepository implements RolRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/roles`;

  listarRoles(): Observable<Rol[]> {
    return this.http.get<RolResponseDto[]>(this.apiUrl).pipe(
      map(dtos => dtos.map(AdminMapper.toRol))
    );
  }

  obtenerPorId(id: number): Observable<Rol> {
    return this.http.get<RolResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map(AdminMapper.toRol)
    );
  }

  registrarRol(nombre: string): Observable<Rol> {
    return this.http.post<RolResponseDto>(this.apiUrl, { nombre }).pipe(
      map(AdminMapper.toRol)
    );
  }

  editarRol(id: number, nombre: string): Observable<Rol> {
    return this.http.put<RolResponseDto>(this.apiUrl, { id, nombre }).pipe(
      map(AdminMapper.toRol)
    );
  }

  eliminarRol(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<Rol[]> {
    return this.http.get<RolResponseDto[]>(`${this.apiUrl}/search/${nombre}`).pipe(
      map(dtos => dtos.map(AdminMapper.toRol))
    );
  }
}
