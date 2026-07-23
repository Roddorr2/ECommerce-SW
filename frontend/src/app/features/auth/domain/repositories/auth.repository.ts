import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Auth } from '../models/auth.model';

export interface AuthRepository {
  login(correo: string, contrasena: string): Observable<Auth>;
  verificarCodigo(correo: string, codigo: string): Observable<Auth>;
  reenviarCodigo(correo: string): Observable<Auth>;
  registrarCliente(nombre: string, telefono: string, correo: string, contrasena: string, direccion: string): Observable<Auth>;
  solicitarRecuperacion(correo: string): Observable<string>;
  confirmarRecuperacion(correo: string, codigo: string, nuevaContrasena: string): Observable<string>;
  cambiarContrasena(contrasenaActual: string, nuevaContrasena: string): Observable<string>;
  
  guardarSesion(auth: Auth): void;
  guardarCorreoPendiente(correo: string): void;
  obtenerCorreoPendiente(): string | null;
  limpiarCorreoPendiente(): void;
  obtenerToken(): string | null;
  obtenerRol(): string | null;
  estaAutenticado(): boolean;
  cerrarSesion(): void;
}

export const AUTH_REPOSITORY = new InjectionToken<AuthRepository>('AuthRepository');
