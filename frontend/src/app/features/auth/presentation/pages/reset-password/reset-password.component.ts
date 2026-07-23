import { Component, inject, OnInit, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
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


const contrasenasCoincidenValidator = (control: AbstractControl): ValidationErrors | null => {
  const nueva = control.get('nuevaContrasena')?.value;
  const confirmar = control.get('confirmarContrasena')?.value;
  return nueva && confirmar && nueva !== confirmar ? { noCoinciden: true } : null;
};

@Component({
  selector: 'app-reset-password',
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
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss',
})
export class ResetPasswordComponent implements OnInit {
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  correo = signal('');
  cargando = signal(false);

  form: FormGroup = this.fb.group(
    {
      codigo: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
      nuevaContrasena: [
        '',
        [Validators.required, Validators.minLength(8), Validators.maxLength(256)],
      ],
      confirmarContrasena: ['', Validators.required],
    },
    { validators: contrasenasCoincidenValidator },
  );

  ngOnInit(): void {
    const correo = this.authRepository.obtenerCorreoPendiente();
    if (!correo) {
      this.router.navigate(['/auth/forgot-password']);
      return;
    }
    this.correo.set(correo);
  }

  confirmar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.cargando.set(true);

    this.authRepository
      .confirmarRecuperacion(
        this.correo(),
        this.form.value.codigo,
        this.form.value.nuevaContrasena
      )
      .subscribe({
        next: (mensaje) => {
          this.cargando.set(false);
          this.authRepository.limpiarCorreoPendiente();
          this.messageService.add({
            severity: 'success',
            summary: '¡Contraseña actualizada!',
            detail: mensaje,
            life: 3000,
          });
          setTimeout(() => this.router.navigate(['/auth/login']), 3000);
        },
        error: (err) => {
          this.cargando.set(false);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: err.error?.mensaje || 'El código es inválido o ha expirado',
            life: 4000,
          });
        },
      });
  }
}
