import { Component, inject, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ToastModule } from 'primeng/toast';
import { AUTH_REPOSITORY } from '../../../features/auth/domain/repositories/auth.repository';
import { PasswordModule } from 'primeng/password';

const contrasenasCoincidenValidator = (control: AbstractControl): ValidationErrors | null => {
  const nueva = control.get('nuevaContrasena')?.value;
  const confirmar = control.get('confirmarContrasena')?.value;
  return nueva && confirmar && nueva !== confirmar ? { noCoinciden: true } : null;
};

@Component({
  selector: 'app-change-password',
  imports: [
    ReactiveFormsModule,
    RouterModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    ToastModule,
    PasswordModule,
  ],
  providers: [MessageService],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss',
})
export class ChangePasswordComponent {
  private authRepository = inject(AUTH_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  cargando = signal(false);

  form: FormGroup = this.fb.group(
    {
      contrasenaActual: ['', [Validators.required]],
      nuevaContrasena: ['', [Validators.required, Validators.minLength(8)]],
      confirmarContrasena: ['', [Validators.required]],
    },
    { validators: contrasenasCoincidenValidator },
  );

  cambiar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.cargando.set(true);

    this.authRepository.cambiarContrasena(this.form.value.contrasenaActual, this.form.value.nuevaContrasena).subscribe({
      next: (mensaje) => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Éxito',
          detail: mensaje,
          life: 3000,
        });

        this.form.reset();
      },
      error: (err) => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo cambiar la contraseña',
          life: 4000,
        });
      },
    });
  }
}
