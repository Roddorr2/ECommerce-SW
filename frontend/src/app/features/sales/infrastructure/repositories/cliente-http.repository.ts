import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Cliente } from '../../domain/models/cliente.model';
import { ClienteRepository, RegistrarClienteData, EditarClienteData } from '../../domain/repositories/cliente.repository';
import { ClienteResponseDto } from '../dtos/sales.dto';
import { SalesMapper } from '../mappers/sales.mapper';

@Injectable({
  providedIn: 'root'
})
export class ClienteHttpRepository implements ClienteRepository {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/clientes`;

  public listarClientes(): Observable<Cliente[]> {
    return this.http.get<ClienteResponseDto[]>(this.apiUrl).pipe(map(dtos => SalesMapper.toClienteDomainList(dtos)));
  }

  public obtenerPorId(id: number): Observable<Cliente> {
    return this.http.get<ClienteResponseDto>(`${this.apiUrl}/${id}`).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public obtenerPorUsuarioId(usuarioId: number): Observable<Cliente> {
    return this.http.get<ClienteResponseDto>(`${this.apiUrl}/usuario/${usuarioId}`).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public registrarCliente(data: RegistrarClienteData): Observable<Cliente> {
    const request = SalesMapper.toCrearClienteRequest(data);
    return this.http.post<ClienteResponseDto>(this.apiUrl, request).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public editarCliente(data: EditarClienteData): Observable<Cliente> {
    const request = SalesMapper.toActualizarClienteRequest(data);
    return this.http.put<ClienteResponseDto>(this.apiUrl, request).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public actualizarPerfil(data: EditarClienteData): Observable<Cliente> {
    const request = SalesMapper.toActualizarClienteRequest(data);
    return this.http.put<ClienteResponseDto>(`${this.apiUrl}/perfil`, request).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public obtenerPerfil(): Observable<Cliente> {
    return this.http.get<ClienteResponseDto>(`${this.apiUrl}/perfil`).pipe(map(dto => SalesMapper.toClienteDomain(dto)));
  }

  public eliminarCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
