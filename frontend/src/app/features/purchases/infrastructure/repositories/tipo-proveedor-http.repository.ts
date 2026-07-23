import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { TipoProveedor } from '../../domain/models/tipo-proveedor.model';
import {
  TipoProveedorRepository,
  CrearTipoProveedorData,
  ActualizarTipoProveedorData
} from '../../domain/repositories/tipo-proveedor.repository';
import { TipoProveedorResponseDto } from '../dtos/purchases.dto';
import { PurchasesMapper } from '../mappers/purchases.mapper';

@Injectable({
  providedIn: 'root'
})
export class TipoProveedorHttpRepository implements TipoProveedorRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/tipos-proveedor`;

  public listarTiposProveedor(): Observable<TipoProveedor[]> {
    return this.http.get<TipoProveedorResponseDto[]>(this.apiUrl)
      .pipe(map(dtos => PurchasesMapper.toTipoProveedorDomainList(dtos)));
  }

  public obtenerPorId(id: number): Observable<TipoProveedor> {
    return this.http.get<TipoProveedorResponseDto>(`${this.apiUrl}/${id}`)
      .pipe(map(dto => PurchasesMapper.toTipoProveedorDomain(dto)));
  }

  public registrarTipoProveedor(data: CrearTipoProveedorData): Observable<TipoProveedor> {
    const request = PurchasesMapper.toCrearTipoProveedorRequest(data);
    return this.http.post<TipoProveedorResponseDto>(this.apiUrl, request)
      .pipe(map(dto => PurchasesMapper.toTipoProveedorDomain(dto)));
  }

  public editarTipoProveedor(data: ActualizarTipoProveedorData): Observable<TipoProveedor> {
    const request = PurchasesMapper.toActualizarTipoProveedorRequest(data);
    return this.http.put<TipoProveedorResponseDto>(this.apiUrl, request)
      .pipe(map(dto => PurchasesMapper.toTipoProveedorDomain(dto)));
  }

  public eliminarTipoProveedor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  public buscarPorNombre(nombre: string): Observable<TipoProveedor[]> {
    return this.http.get<TipoProveedorResponseDto[]>(`${this.apiUrl}/search/${nombre}`)
      .pipe(map(dtos => PurchasesMapper.toTipoProveedorDomainList(dtos)));
  }
}
