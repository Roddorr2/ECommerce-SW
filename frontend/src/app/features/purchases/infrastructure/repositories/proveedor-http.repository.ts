import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Proveedor } from '../../domain/models/proveedor.model';
import {
  ProveedorRepository,
  CrearProveedorData,
  ActualizarProveedorData
} from '../../domain/repositories/proveedor.repository';
import { ProveedorResponseDto } from '../dtos/purchases.dto';
import { PurchasesMapper } from '../mappers/purchases.mapper';

@Injectable({
  providedIn: 'root'
})
export class ProveedorHttpRepository implements ProveedorRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/proveedores`;

  public listarProveedores(): Observable<Proveedor[]> {
    return this.http.get<ProveedorResponseDto[]>(this.apiUrl)
      .pipe(map(dtos => PurchasesMapper.toProveedorDomainList(dtos)));
  }

  public obtenerPorId(id: number): Observable<Proveedor> {
    return this.http.get<ProveedorResponseDto>(`${this.apiUrl}/${id}`)
      .pipe(map(dto => PurchasesMapper.toProveedorDomain(dto)));
  }

  public registrarProveedor(data: CrearProveedorData): Observable<Proveedor> {
    const request = PurchasesMapper.toCrearProveedorRequest(data);
    return this.http.post<ProveedorResponseDto>(this.apiUrl, request)
      .pipe(map(dto => PurchasesMapper.toProveedorDomain(dto)));
  }

  public editarProveedor(data: ActualizarProveedorData): Observable<Proveedor> {
    const request = PurchasesMapper.toActualizarProveedorRequest(data);
    return this.http.put<ProveedorResponseDto>(this.apiUrl, request)
      .pipe(map(dto => PurchasesMapper.toProveedorDomain(dto)));
  }

  public eliminarProveedor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  public buscarPorTipoProveedor(tipoProveedor: string): Observable<Proveedor[]> {
    return this.http.get<ProveedorResponseDto[]>(`${this.apiUrl}/search/${tipoProveedor}`)
      .pipe(map(dtos => PurchasesMapper.toProveedorDomainList(dtos)));
  }
}
