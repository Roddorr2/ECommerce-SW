import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { AUTH_REPOSITORY } from '../../../auth/domain/repositories/auth.repository';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-cambiar-contrasena',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, PasswordModule, ToastModule],
  providers: [MessageService],
  templateUrl: './cambiar-contrasena.component.html',
  styleUrl: './cambiar-contrasena.component.scss'
})
export class CambiarContrasenaComponent {
  private authRepository = inject(AUTH_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  guardando = signal(false);

  form: FormGroup = this.fb.group({
    contrasenaActual: ['', Validators.required],
    nuevaContrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(256)]],
    confirmarContrasena: ['', Validators.required]
  }, { validators: this.matchValidator });

  matchValidator(group: AbstractControl): ValidationErrors | null {
    const nueva = group.get('nuevaContrasena')?.value;
    const confirmar = group.get('confirmarContrasena')?.value;
    if (nueva && confirmar && nueva !== confirmar) {
      group.get('confirmarContrasena')?.setErrors({ noCoincide: true });
      return { noCoincide: true };
    }
    return null;
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const { contrasenaActual, nuevaContrasena } = this.form.value;

    this.authRepository.cambiarContrasena(contrasenaActual, nuevaContrasena).subscribe({
      next: (msg) => {
        this.guardando.set(false);
        this.form.reset();
        this.messageService.add({
          severity: 'success',
          summary: 'Contraseña cambiada',
          detail: msg || 'Contraseña actualizada con éxito.',
          life: 3000
        });
      },
      error: (err) => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo cambiar la contraseña. Verifique su contraseña actual.',
          life: 4000
        });
      }
    });
  }
}
