import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Usuario } from '../../domain/models/usuario.model';
import { UsuarioRepository } from '../../domain/repositories/usuario.repository';
import { UsuarioResponseDto } from '../dtos/admin.dto';
import { AdminMapper } from '../mappers/admin.mapper';

@Injectable({
  providedIn: 'root'
})
export class UsuarioHttpRepository implements UsuarioRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/usuarios`;

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<UsuarioResponseDto[]>(this.apiUrl).pipe(
      map(dtos => dtos.map(AdminMapper.toUsuario))
    );
  }

  obtenerPorId(id: number): Observable<Usuario> {
    return this.http.get<UsuarioResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map(AdminMapper.toUsuario)
    );
  }

  registrarUsuario(
    nombre: string,
    correo: string,
    contrasena: string,
    activo: boolean,
    rolId: number
  ): Observable<Usuario> {
    return this.http.post<UsuarioResponseDto>(this.apiUrl, {
      nombre,
      correo,
      contrasena,
      activo,
      rolId
    }).pipe(map(AdminMapper.toUsuario));
  }

  editarUsuario(
    id: number,
    nombre: string,
    correo: string,
    activo: boolean,
    rolId: number
  ): Observable<Usuario> {
    return this.http.put<UsuarioResponseDto>(this.apiUrl, {
      id,
      nombre,
      correo,
      activo,
      rolId
    }).pipe(map(AdminMapper.toUsuario));
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
