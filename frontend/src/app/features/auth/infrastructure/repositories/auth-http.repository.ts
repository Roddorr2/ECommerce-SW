import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environment/environment';
import { Auth } from '../../domain/models/auth.model';
import { AuthRepository } from '../../domain/repositories/auth.repository';
import { AuthMapper } from '../mappers/auth.mapper';
import {
  AuthResponseDto,
  SolicitudCambioContrasenaResponseDto,
  CambioContrasenaResponseDto
} from '../dtos/auth.dto';

@Injectable({ providedIn: 'root' })
export class AuthHttpRepository implements AuthRepository {
  private apiUrl = `${environment.apiUrl}/auth`;
  private http = inject(HttpClient);

  login(correo: string, contrasena: string): Observable<Auth> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/login`, { correo, contrasena })
      .pipe(map(AuthMapper.toDomain));
  }

  verificarCodigo(correo: string, codigo: string): Observable<Auth> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/login/verify`, { correo, codigo })
      .pipe(map(AuthMapper.toDomain));
  }

  reenviarCodigo(correo: string): Observable<Auth> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/resend-code`, { correo })
      .pipe(map(AuthMapper.toDomain));
  }

  registrarCliente(
    nombre: string,
    telefono: string,
    correo: string,
    contrasena: string,
    direccion: string
  ): Observable<Auth> {
    return this.http.post<AuthResponseDto>(`${this.apiUrl}/register`, { nombre, telefono, correo, contrasena, direccion })
      .pipe(map(AuthMapper.toDomain));
  }

  solicitarRecuperacion(correo: string): Observable<string> {
    return this.http.post<SolicitudCambioContrasenaResponseDto>(`${this.apiUrl}/forgot-password`, { correo })
      .pipe(map(res => res.mensaje));
  }

  confirmarRecuperacion(correo: string, codigo: string, nuevaContrasena: string): Observable<string> {
    return this.http.post<CambioContrasenaResponseDto>(`${this.apiUrl}/reset-password`, { correo, codigo, nuevaContrasena })
      .pipe(map(res => res.mensaje));
  }

  cambiarContrasena(contrasenaActual: string, nuevaContrasena: string): Observable<string> {
    return this.http.put<CambioContrasenaResponseDto>(`${this.apiUrl}/change-password`, { contrasenaActual, nuevaContrasena })
      .pipe(map(res => res.mensaje));
  }

  guardarSesion(auth: Auth): void {
    if (auth.token) {
      localStorage.setItem(environment.jwtKey, auth.token);
    }
    if (auth.rol) {
      localStorage.setItem('rol', auth.normalizadoRol || auth.rol);
    }
    if (auth.nombre) {
      localStorage.setItem('nombre', auth.nombre);
    }
  }

  guardarCorreoPendiente(correo: string): void {
    localStorage.setItem('correo_2fa', correo);
  }

  obtenerCorreoPendiente(): string | null {
    return localStorage.getItem('correo_2fa');
  }

  limpiarCorreoPendiente(): void {
    localStorage.removeItem('correo_2fa');
  }

  obtenerToken(): string | null {
    return localStorage.getItem(environment.jwtKey);
  }

  obtenerRol(): string | null {
    return localStorage.getItem('rol');
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }

  cerrarSesion(): void {
    localStorage.clear();
  }
}
