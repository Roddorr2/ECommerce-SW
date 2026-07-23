import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

export interface RegistrarClienteData {
  usuarioId: number;
  correo: string;
  telefono: string;
  direccion: string;
}

export interface EditarClienteData {
  id: number;
  usuarioId: number;
  correo: string;
  telefono: string;
  direccion: string;
}

export interface ClienteRepository {
  listarClientes(): Observable<Cliente[]>;
  obtenerPorId(id: number): Observable<Cliente>;
  obtenerPorUsuarioId(usuarioId: number): Observable<Cliente>;
  registrarCliente(data: RegistrarClienteData): Observable<Cliente>;
  editarCliente(data: EditarClienteData): Observable<Cliente>;
  actualizarPerfil(data: EditarClienteData): Observable<Cliente>;
  obtenerPerfil(): Observable<Cliente>;
  eliminarCliente(id: number): Observable<void>;
}

export const CLIENTE_REPOSITORY = new InjectionToken<ClienteRepository>('ClienteRepository');
