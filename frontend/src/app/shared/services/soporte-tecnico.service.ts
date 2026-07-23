import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';

export interface CrearTicketRequest {
  nombre: string;
  email: string;
  tipoCaso: string;
  asunto: string;
  mensaje: string;
}

export interface TicketSoporteResponse {
  id: number;
  codigoTicket: string;
  nombreCliente: string;
  emailCliente: string;
  tipoSolicitud: string;
  asunto: string;
  descripcion: string;
  estado: string;
  fechaCreacion: string;
  fechaUltimaActualizacion: string;
}

@Injectable({
  providedIn: 'root'
})
export class SoporteTecnicoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/soporte`;

  crearTicket(ticket: CrearTicketRequest): Observable<TicketSoporteResponse> {
    return this.http.post<TicketSoporteResponse>(this.apiUrl, ticket);
  }

  obtenerPorCodigo(codigo: string): Observable<TicketSoporteResponse> {
    return this.http.get<TicketSoporteResponse>(`${this.apiUrl}/${codigo}`);
  }

  listarTodos(): Observable<TicketSoporteResponse[]> {
    return this.http.get<TicketSoporteResponse[]>(this.apiUrl);
  }
}
