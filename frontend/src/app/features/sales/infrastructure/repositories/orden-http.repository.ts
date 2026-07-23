import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Orden, OrdenResumen } from '../../domain/models/orden.model';
import { OrdenRepository, CrearOrdenDesdeCarritoData, CambiarEstadoOrdenData } from '../../domain/repositories/orden.repository';
import { OrdenResponseDto, OrdenResumenResponseDto } from '../dtos/sales.dto';
import { SalesMapper } from '../mappers/sales.mapper';
import { EstadoOrden } from '../../domain/models/estado-orden.enum';

@Injectable({
  providedIn: 'root'
})
export class OrdenHttpRepository implements OrdenRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/ordenes`;

  public crearOrdenDesdeCarrito(data: CrearOrdenDesdeCarritoData): Observable<Orden> {
    const request = SalesMapper.toCrearOrdenRequest(data);
    return this.http.post<OrdenResponseDto>(this.apiUrl, request).pipe(map(dto => SalesMapper.toOrdenDomain(dto)));
  }

  public obtenerPorId(ordenId: number): Observable<Orden> {
    return this.http.get<OrdenResponseDto>(`${this.apiUrl}/${ordenId}`).pipe(map(dto => SalesMapper.toOrdenDomain(dto)));
  }

  public obtenerMisOrdenes(): Observable<OrdenResumen[]> {
    return this.http.get<OrdenResumenResponseDto[]>(`${this.apiUrl}/mis-ordenes`).pipe(map(dtos => SalesMapper.toOrdenResumenDomainList(dtos)));
  }

  public listarOrdenes(): Observable<OrdenResumen[]> {
    return this.http.get<OrdenResumenResponseDto[]>(`${this.apiUrl}/ordenes`).pipe(map(dtos => SalesMapper.toOrdenResumenDomainList(dtos)));
  }

  public obtenerPorCliente(clienteId: number): Observable<OrdenResumen[]> {
    return this.http.get<OrdenResumenResponseDto[]>(`${this.apiUrl}/cliente/${clienteId}`).pipe(map(dtos => SalesMapper.toOrdenResumenDomainList(dtos)));
  }

  public obtenerPorEstado(estado: EstadoOrden): Observable<OrdenResumen[]> {
    return this.http.get<OrdenResumenResponseDto[]>(`${this.apiUrl}/estado/${estado}`).pipe(map(dtos => SalesMapper.toOrdenResumenDomainList(dtos)));
  }

  public cambiarEstado(ordenId: number, data: CambiarEstadoOrdenData): Observable<Orden> {
    const request = SalesMapper.toCambiarEstadoRequest(data);
    return this.http.put<OrdenResponseDto>(`${this.apiUrl}/${ordenId}/estado`, request).pipe(map(dto => SalesMapper.toOrdenDomain(dto)));
  }

  public cancelarOrden(ordenId: number, motivo?: string): Observable<Orden> {
    return this.http.post<OrdenResponseDto>(
      `${this.apiUrl}/${ordenId}/cancelar`,
      {},
      { params: motivo ? { motivo } : {} }
    ).pipe(map(dto => SalesMapper.toOrdenDomain(dto)));
  }
}
