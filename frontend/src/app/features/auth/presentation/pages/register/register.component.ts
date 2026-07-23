import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AUTH_REPOSITORY } from '../../../domain/repositories/auth.repository';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    RouterModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    PasswordModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private messageService = inject(MessageService);
  cargando = false;
  form: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
    contrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(256)]],
    telefono: ['', [Validators.required, Validators.pattern('^[0-9+\\-\\s]{6,20}$')]],
    direccion: ['', [Validators.required, Validators.maxLength(200)]],
  });

  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { nombre, telefono, correo, contrasena, direccion } = this.form.value;
    this.cargando = true;

    this.authRepository.registrarCliente(nombre, telefono, correo, contrasena, direccion).subscribe({
      next: () => {
        this.cargando = false;
        this.messageService.add({
          severity: 'success',
          summary: '¡Registro exitoso!',
          detail: 'Tu cuenta ha sido creada. Redirigiendo al login...',
          life: 2500,
        });
        setTimeout(() => this.router.navigate(['/auth/login']), 2500);
      },
      error: (err) => {
        this.cargando = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error al registrar',
          detail: err.error?.mensaje || 'Ocurrió un error. Intenta nuevamente.',
          life: 4000,
        });
      },
    });
  }
}
