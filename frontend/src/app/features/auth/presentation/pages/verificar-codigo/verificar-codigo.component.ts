import { Component, ElementRef, inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { AUTH_REPOSITORY } from '../../../domain/repositories/auth.repository';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-verificar-codigo',
  imports: [RouterModule, ButtonModule, CardModule, ToastModule],
  providers: [MessageService],
  templateUrl: './verificar-codigo.component.html',
  styleUrl: './verificar-codigo.component.scss',
})
export class VerificarCodigoComponent implements OnInit {
  @ViewChildren('digitInput') digitInputs!: QueryList<ElementRef>;

  digits: string[] = ['', '', '', '', '', ''];
  cargando: boolean = false;
  reenviando: boolean = false;
  correo: string = '';
  tiempoRestante = 0;
  private intervalo?: ReturnType<typeof setInterval>;

  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private messageService = inject(MessageService);

  ngOnInit(): void {
    const correo = this.authRepository.obtenerCorreoPendiente();
    if (!correo) {
      this.router.navigate(['/auth/login']);
      return;
    }
    this.correo = correo;
    this.iniciarContador(600);
  }

  onInput(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/\D/g, '').slice(-1);
    this.digits[index] = value;

    if (value && index < 5) {
      this.digitInputs.toArray()[index + 1].nativeElement.focus();
    }
  }

  onKeydown(event: KeyboardEvent, index: number): void {
    if (event.key === 'Backspace' && !this.digits[index] && index > 0) {
      this.digitInputs.toArray()[index - 1].nativeElement.focus();
    }
  }

  onPaste(event: ClipboardEvent): void {
    event.preventDefault();
    const pasted = event.clipboardData?.getData('text').replace(/\D/g, '').slice(0, 6) ?? '';
    pasted.split('').forEach((char, i) => {
      this.digits[i] = char;
      const input = this.digitInputs.toArray()[i];
      if (input) input.nativeElement.value = char;
    });
    const lastIndex = Math.min(pasted.length, 5);
    this.digitInputs.toArray()[lastIndex].nativeElement.focus();
  }

  get codigoCompleto(): string {
    return this.digits.join('');
  }

  get formularioValido(): boolean {
    return this.codigoCompleto.length === 6;
  }

  verificar(): void {
    if (!this.formularioValido) return;

    this.cargando = true;
    this.authRepository
      .verificarCodigo(this.correo, this.codigoCompleto)
      .subscribe({
        next: (res) => {
          this.cargando = false;
          this.authRepository.guardarSesion(res);
          this.authRepository.limpiarCorreoPendiente();
          this.detenerContador();
          this.redirigirSegunRol(res.rol);
        },
        error: (err) => {
          this.cargando = false;
          this.messageService.add({
            severity: 'error',
            summary: 'Código inválido',
            detail: err.error?.mensaje || 'El código es incorrecto o ha expirado.',
            life: 4000,
          });
        },
      });
  }

  reenviar(): void {
    this.reenviando = true;
    this.authRepository.reenviarCodigo(this.correo).subscribe({
      next: () => {
        this.reenviando = false;
        this.digits = ['', '', '', '', '', ''];
        this.digitInputs.toArray().forEach((i) => (i.nativeElement.value = ''));
        this.digitInputs.toArray()[0].nativeElement.focus();
        this.iniciarContador(600);
        this.messageService.add({
          severity: 'success',
          summary: 'Código reenviado',
          detail: 'Revisa tu correo electrónico',
          life: 3000,
        });
      },
      error: (err) => {
        this.reenviando = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo reenviar el código.',
          life: 4000,
        });
      },
    });
  }

  get tiempoFormateado(): string {
    const min = Math.floor(this.tiempoRestante / 60)
      .toString()
      .padStart(2, '0');
    const seg = (this.tiempoRestante % 60).toString().padStart(2, '0');
    return `${min}:${seg}`;
  }

  private iniciarContador(segundos: number): void {
    this.detenerContador();
    this.tiempoRestante = segundos;
    this.intervalo = setInterval(() => {
      this.tiempoRestante--;
      if (this.tiempoRestante <= 0) this.detenerContador();
    }, 1000);
  }

  private detenerContador(): void {
    if (this.intervalo) clearInterval(this.intervalo);
  }

  private redirigirSegunRol(rol: string | null): void {
    const rolUpper = rol?.toUpperCase();
    if (rolUpper === 'ADMINISTRADOR' || rolUpper === 'EMPLEADO') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/cliente/productos']);
    }
  }

  ngOnDestroy(): void {
    this.detenerContador();
  }
}
