import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Categoria } from '../../domain/models/categoria.model';
import { CategoriaRepository } from '../../domain/repositories/categoria.repository';
import { CategoriaResponseDto } from '../dtos/categoria.dto';
import { CategoriaMapper } from '../mappers/categoria.mapper';

@Injectable({
  providedIn: 'root',
})
export class CategoriaHttpRepository implements CategoriaRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/categorias`;

  public listarCategorias(): Observable<Categoria[]> {
    return this.http.get<CategoriaResponseDto[]>(this.apiUrl).pipe(
      map((dtos) => CategoriaMapper.toDomainList(dtos))
    );
  }

  public obtenerPorId(id: number): Observable<Categoria> {
    return this.http.get<CategoriaResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map((dto) => CategoriaMapper.toDomain(dto))
    );
  }

  public registrarCategoria(nombre: string): Observable<Categoria> {
    const request = { nombre };
    return this.http.post<CategoriaResponseDto>(this.apiUrl, request).pipe(
      map((dto) => CategoriaMapper.toDomain(dto))
    );
  }

  public editarCategoria(id: number, nombre: string): Observable<Categoria> {
    const request = { id, nombre };
    return this.http.put<CategoriaResponseDto>(this.apiUrl, request).pipe(
      map((dto) => CategoriaMapper.toDomain(dto))
    );
  }

  public eliminarCategoria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  public buscarPorNombre(nombre: string): Observable<Categoria[]> {
    return this.http.get<CategoriaResponseDto[]>(`${this.apiUrl}/search/${nombre}`).pipe(
      map((dtos) => CategoriaMapper.toDomainList(dtos))
    );
  }
}
