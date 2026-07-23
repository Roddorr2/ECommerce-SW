import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AUTH_REPOSITORY } from '../../../domain/repositories/auth.repository';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LucideAngularModule } from 'lucide-angular';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-login',
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
    LucideAngularModule,
  ],
  providers: [MessageService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  cargando = false;
  private fb = inject(FormBuilder);
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private messageService = inject(MessageService);
  form: FormGroup = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', Validators.required],
  });

  ngOnInit(): void {
    if (this.authRepository.estaAutenticado()) {
      this.redirigirSegunRol(this.authRepository.obtenerRol());
    }
  }

  login(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { correo, contrasena } = this.form.value;
    this.cargando = true;

    this.authRepository.login(correo, contrasena).subscribe({
      next: (res) => {
        this.cargando = false;

        if (res.requiere2FA) {
          this.authRepository.guardarCorreoPendiente(correo);
          this.router.navigate(['/auth/verificar-codigo']);
          return;
        }

        this.authRepository.guardarSesion(res);
        this.redirigirSegunRol(res.rol);
      },
      error: (err) => {
        this.cargando = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'Credenciales incorrectas.',
          life: 4000,
        });
      },
    });
  }

  private redirigirSegunRol(rol: string | null): void {
    const rolUpper = rol?.toUpperCase();
    if (rolUpper === 'ADMINISTRADOR' || rolUpper === 'EMPLEADO') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/cliente/productos']);
    }
  }
}
