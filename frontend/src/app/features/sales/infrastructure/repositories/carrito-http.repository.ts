import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Carrito } from '../../domain/models/carrito.model';
import { CarritoRepository, AgregarItemCarritoData, ActualizarItemCarritoData } from '../../domain/repositories/carrito.repository';
import { CarritoResponseDto } from '../dtos/sales.dto';
import { SalesMapper } from '../mappers/sales.mapper';

@Injectable({
  providedIn: 'root'
})
export class CarritoHttpRepository implements CarritoRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/carrito`;

  public obtenerCarritoActivo(): Observable<Carrito> {
    return this.http.get<CarritoResponseDto>(this.apiUrl).pipe(map(dto => SalesMapper.toCarritoDomain(dto)));
  }

  public agregarItem(data: AgregarItemCarritoData): Observable<Carrito> {
    const request = SalesMapper.toAgregarItemRequest(data);
    return this.http.post<CarritoResponseDto>(`${this.apiUrl}/items`, request).pipe(map(dto => SalesMapper.toCarritoDomain(dto)));
  }

  public actualizarItem(itemId: number, data: ActualizarItemCarritoData): Observable<Carrito> {
    const request = SalesMapper.toActualizarItemRequest(data);
    return this.http.put<CarritoResponseDto>(`${this.apiUrl}/items/${itemId}`, request).pipe(map(dto => SalesMapper.toCarritoDomain(dto)));
  }

  public eliminarItem(itemId: number): Observable<Carrito> {
    return this.http.delete<CarritoResponseDto>(`${this.apiUrl}/items/${itemId}`).pipe(map(dto => SalesMapper.toCarritoDomain(dto)));
  }

  public vaciarCarrito(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/vaciar`);
  }
}
