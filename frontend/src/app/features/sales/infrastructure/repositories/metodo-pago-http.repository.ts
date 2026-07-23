import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { MetodoPago } from '../../domain/models/metodo-pago.model';
import { MetodoPagoRepository, CrearMetodoPagoData, ActualizarMetodoPagoData } from '../../domain/repositories/metodo-pago.repository';
import { MetodoPagoResponseDto } from '../dtos/sales.dto';
import { SalesMapper } from '../mappers/sales.mapper';

@Injectable({
  providedIn: 'root'
})
export class MetodoPagoHttpRepository implements MetodoPagoRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/metodos-pago`;

  public listarMetodosPago(): Observable<MetodoPago[]> {
    return this.http.get<MetodoPagoResponseDto[]>(this.apiUrl).pipe(map(dtos => SalesMapper.toMetodoPagoDomainList(dtos)));
  }

  public obtenerPorId(id: number): Observable<MetodoPago> {
    return this.http.get<MetodoPagoResponseDto>(`${this.apiUrl}/${id}`).pipe(map(dto => SalesMapper.toMetodoPagoDomain(dto)));
  }

  public registrarMetodoPago(data: CrearMetodoPagoData): Observable<MetodoPago> {
    const request = SalesMapper.toCrearMetodoPagoRequest(data);
    return this.http.post<MetodoPagoResponseDto>(this.apiUrl, request).pipe(map(dto => SalesMapper.toMetodoPagoDomain(dto)));
  }

  public editarMetodoPago(data: ActualizarMetodoPagoData): Observable<MetodoPago> {
    const request = SalesMapper.toActualizarMetodoPagoRequest(data);
    return this.http.put<MetodoPagoResponseDto>(this.apiUrl, request).pipe(map(dto => SalesMapper.toMetodoPagoDomain(dto)));
  }

  public eliminarMetodoPago(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  public buscarPorNombre(nombre: string): Observable<MetodoPago[]> {
    return this.http.get<MetodoPagoResponseDto[]>(`${this.apiUrl}/search/${nombre}`).pipe(map(dtos => SalesMapper.toMetodoPagoDomainList(dtos)));
  }
}
