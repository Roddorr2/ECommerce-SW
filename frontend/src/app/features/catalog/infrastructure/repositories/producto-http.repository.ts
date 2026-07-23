import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Producto } from '../../domain/models/producto.model';
import { ProductoRepository, RegistrarProductoData, EditarProductoData } from '../../domain/repositories/producto.repository';
import { ProductoResponseDto, ProductoImagenResponseDto } from '../dtos/producto.dto';
import { ProductoMapper } from '../mappers/producto.mapper';

@Injectable({
  providedIn: 'root',
})
export class ProductoHttpRepository implements ProductoRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/productos`;

  public listarProductos(): Observable<Producto[]> {
    return this.http.get<ProductoResponseDto[]>(this.apiUrl).pipe(
      map((dtos) => ProductoMapper.toDomainList(dtos))
    );
  }

  public obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<ProductoResponseDto>(`${this.apiUrl}/${id}`).pipe(
      map((dto) => ProductoMapper.toDomain(dto))
    );
  }

  public registrarProducto(data: RegistrarProductoData): Observable<Producto> {
    const request = ProductoMapper.toCreateRequest(data);
    return this.http.post<ProductoResponseDto>(this.apiUrl, request).pipe(
      map((dto) => ProductoMapper.toDomain(dto))
    );
  }

  public editarProducto(data: EditarProductoData): Observable<Producto> {
    const request = ProductoMapper.toUpdateRequest(data);
    return this.http.put<ProductoResponseDto>(this.apiUrl, request).pipe(
      map((dto) => ProductoMapper.toDomain(dto))
    );
  }

  public eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  public subirImagen(archivo: File): Observable<{ nombreArchivo: string }> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ProductoImagenResponseDto>(`${this.apiUrl}/imagenes`, formData);
  }

  public buscarProductosDisponibles(nombre: string): Observable<Producto[]> {
    return this.http.get<ProductoResponseDto[]>(`${this.apiUrl}/search/${nombre}`).pipe(
      map((dtos) => ProductoMapper.toDomainList(dtos))
    );
  }

  public obtenerUrlImagen(imagenNombre: string): string {
    return `${environment.imageUrl}/${imagenNombre}`;
  }
}
