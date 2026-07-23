import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

export interface UsuarioRepository {
  listarUsuarios(): Observable<Usuario[]>;
  obtenerPorId(id: number): Observable<Usuario>;
  registrarUsuario(nombre: string, correo: string, contrasena: string, activo: boolean, rolId: number): Observable<Usuario>;
  editarUsuario(id: number, nombre: string, correo: string, activo: boolean, rolId: number): Observable<Usuario>;
  eliminarUsuario(id: number): Observable<void>;
}

export const USUARIO_REPOSITORY = new InjectionToken<UsuarioRepository>('UsuarioRepository');
