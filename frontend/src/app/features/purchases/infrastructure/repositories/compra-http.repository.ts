import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Compra, CompraResumen } from '../../domain/models/compra.model';
import { EstadoCompra } from '../../domain/models/estado-compra.enum';
import {
  CompraRepository,
  CrearCompraData,
  CambiarEstadoCompraData
} from '../../domain/repositories/compra.repository';
import { CompraResponseDto, CompraResumenResponseDto } from '../dtos/purchases.dto';
import { PurchasesMapper } from '../mappers/purchases.mapper';

@Injectable({
  providedIn: 'root'
})
export class CompraHttpRepository implements CompraRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/compras`;

  public listarCompras(): Observable<CompraResumen[]> {
    return this.http.get<CompraResumenResponseDto[]>(this.apiUrl)
      .pipe(map(dtos => PurchasesMapper.toCompraResumenDomainList(dtos)));
  }

  public obtenerPorId(id: number): Observable<Compra> {
    return this.http.get<CompraResponseDto>(`${this.apiUrl}/${id}`)
      .pipe(map(dto => PurchasesMapper.toCompraDomain(dto)));
  }

  public crearCompra(data: CrearCompraData): Observable<Compra> {
    const request = PurchasesMapper.toCrearCompraRequest(data);
    return this.http.post<CompraResponseDto>(this.apiUrl, request)
      .pipe(map(dto => PurchasesMapper.toCompraDomain(dto)));
  }

  public recibirCompra(compraId: number): Observable<Compra> {
    return this.http.post<CompraResponseDto>(`${this.apiUrl}/${compraId}/recibir`, {})
      .pipe(map(dto => PurchasesMapper.toCompraDomain(dto)));
  }

  public cancelarCompra(compraId: number, motivo?: string): Observable<Compra> {
    return this.http.post<CompraResponseDto>(
      `${this.apiUrl}/${compraId}/cancelar`,
      {},
      { params: motivo ? { motivo } : {} }
    ).pipe(map(dto => PurchasesMapper.toCompraDomain(dto)));
  }

  public cambiarEstado(compraId: number, data: CambiarEstadoCompraData): Observable<Compra> {
    const request = PurchasesMapper.toCambiarEstadoRequest(data);
    return this.http.put<CompraResponseDto>(`${this.apiUrl}/${compraId}/estado`, request)
      .pipe(map(dto => PurchasesMapper.toCompraDomain(dto)));
  }

  public obtenerPorProveedor(proveedorId: number): Observable<CompraResumen[]> {
    return this.http.get<CompraResumenResponseDto[]>(`${this.apiUrl}/proveedor/${proveedorId}`)
      .pipe(map(dtos => PurchasesMapper.toCompraResumenDomainList(dtos)));
  }

  public obtenerPorEstado(estado: EstadoCompra): Observable<CompraResumen[]> {
    return this.http.get<CompraResumenResponseDto[]>(`${this.apiUrl}/estado/${estado}`)
      .pipe(map(dtos => PurchasesMapper.toCompraResumenDomainList(dtos)));
  }

  public eliminarCompra(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
