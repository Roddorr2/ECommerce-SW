import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AUTH_REPOSITORY } from '../../../../auth/domain/repositories/auth.repository';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';

@Component({
  selector: 'app-perfil-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ToastModule,
    ButtonModule,
    InputTextModule,
    PasswordModule
  ],
  providers: [MessageService],
  templateUrl: './perfil-admin.component.html',
  styleUrl: './perfil-admin.component.scss'
})
export class PerfilAdminComponent implements OnInit {
  private authRepository = inject(AUTH_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  public nombreUsuario = signal<string>('');
  public correoUsuario = signal<string>('');
  public rolUsuario = signal<string>('');
  public guardando = signal<boolean>(false);
  public mostrandoCambiarContrasena = signal<boolean>(false);

  public passwordForm: FormGroup = this.fb.group({
    contrasenaActual: ['', [Validators.required]],
    nuevaContrasena: ['', [Validators.required, Validators.minLength(8)]],
    confirmarContrasena: ['', [Validators.required]]
  });

  ngOnInit(): void {
    this.cargarDatosUsuario();
  }

  private cargarDatosUsuario(): void {
    const nombre = localStorage.getItem('nombre') || 'Administrador';
    const rol = this.authRepository.obtenerRol() || localStorage.getItem('rol') || 'Administrador';
    const token = this.authRepository.obtenerToken();

    let correo = 'admin@technologyfix.com';
    if (token) {
      try {
        const payloadBase64 = token.split('.')[1];
        const payloadJson = atob(payloadBase64);
        const payload = JSON.parse(payloadJson);
        if (payload.sub) {
          correo = payload.sub;
        }
      } catch (e) {
        console.warn('No se pudo decodificar el correo del token:', e);
      }
    }

    this.nombreUsuario.set(nombre);
    this.rolUsuario.set(rol);
    this.correoUsuario.set(correo);
  }

  public toggleCambiarContrasena(): void {
    this.mostrandoCambiarContrasena.set(!this.mostrandoCambiarContrasena());
    if (!this.mostrandoCambiarContrasena()) {
      this.passwordForm.reset();
    }
  }

  public cambiarContrasena(): void {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    const { contrasenaActual, nuevaContrasena, confirmarContrasena } = this.passwordForm.value;

    if (nuevaContrasena !== confirmarContrasena) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Contraseñas no coinciden',
        detail: 'La nueva contraseña y la confirmación deben ser idénticas.'
      });
      return;
    }

    this.guardando.set(true);

    this.authRepository.cambiarContrasena(contrasenaActual, nuevaContrasena).subscribe({
      next: (mensaje) => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Contraseña Actualizada',
          detail: mensaje || 'Tu contraseña ha sido actualizada exitosamente.'
        });
        this.passwordForm.reset();
        this.mostrandoCambiarContrasena.set(false);
      },
      error: (err) => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo actualizar la contraseña. Verifica tu contraseña actual.'
        });
      }
    });
  }
}
