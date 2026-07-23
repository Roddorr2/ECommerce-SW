import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';

export interface CategoriaRepository {
  listarCategorias(): Observable<Categoria[]>;
  obtenerPorId(id: number): Observable<Categoria>;
  registrarCategoria(nombre: string): Observable<Categoria>;
  editarCategoria(id: number, nombre: string): Observable<Categoria>;
  eliminarCategoria(id: number): Observable<void>;
  buscarPorNombre(nombre: string): Observable<Categoria[]>;
}

export const CATEGORIA_REPOSITORY = new InjectionToken<CategoriaRepository>('CategoriaRepository');
