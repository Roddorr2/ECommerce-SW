import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { CardModule } from 'primeng/card';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AUTH_REPOSITORY } from '../../../domain/repositories/auth.repository';

@Component({
  selector: 'app-forgot-password',
  imports: [
    ReactiveFormsModule,
    RouterModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
})
export class ForgotPasswordComponent {
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  form: FormGroup = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
  });

  cargando = signal(false);

  enviar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.cargando.set(true);

    this.authRepository.solicitarRecuperacion(this.form.value.correo).subscribe({
      next: (mensaje) => {
        this.cargando.set(false);
        this.authRepository.guardarCorreoPendiente(this.form.value.correo);
        this.messageService.add({
          severity: 'success',
          summary: 'Correo enviado',
          detail: mensaje,
          life: 3000,
        });
        setTimeout(() => this.router.navigate(['/auth/reset-password']), 3000);
      },
      error: (err) => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo enviar el correo.',
          life: 4000,
        });
      },
    });
  }
}
