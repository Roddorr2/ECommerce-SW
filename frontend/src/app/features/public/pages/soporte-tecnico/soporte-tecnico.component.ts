import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { SoporteTecnicoService } from '../../../../shared/services/soporte-tecnico.service';

@Component({
  selector: 'app-soporte-tecnico',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent, FooterComponent],
  templateUrl: './soporte-tecnico.component.html',
  styleUrl: './soporte-tecnico.component.scss'
})
export class SoporteTecnicoComponent {
  private soporteService = inject(SoporteTecnicoService);

  public formulario = {
    nombre: '',
    email: '',
    asunto: '',
    tipoCaso: 'duda',
    mensaje: ''
  };

  public enviando = signal<boolean>(false);

  public enviarFormulario(): void {
    if (!this.formulario.nombre || !this.formulario.email || !this.formulario.asunto || !this.formulario.mensaje) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Por favor, completa todos los campos del formulario antes de continuar.',
        confirmButtonColor: '#6A5ACD'
      });
      return;
    }

    this.enviando.set(true);

    this.soporteService.crearTicket({
      nombre: this.formulario.nombre,
      email: this.formulario.email,
      tipoCaso: this.formulario.tipoCaso,
      asunto: this.formulario.asunto,
      mensaje: this.formulario.mensaje
    }).subscribe({
      next: (response) => {
        this.enviando.set(false);

        Swal.fire({
          icon: 'success',
          title: '¡Solicitud Registrada!',
          html: `<p class="text-sm">Hemos recibido tu solicitud de soporte técnico.</p>
                 <p class="mt-2 text-xs text-slate-500">Código de Ticket: <strong class="text-brand-primary font-bold">${response.codigoTicket}</strong></p>
                 <p class="mt-1 text-xs text-slate-400">Guarda este código para hacer seguimiento a tu reclamo.</p>`,
          confirmButtonColor: '#6A5ACD'
        });

        // Limpiar formulario
        this.formulario = {
          nombre: '',
          email: '',
          asunto: '',
          tipoCaso: 'duda',
          mensaje: ''
        };
      },
      error: (err) => {
        this.enviando.set(false);
        console.error('Error al registrar ticket de soporte:', err);

        Swal.fire({
          icon: 'error',
          title: 'Error al enviar',
          text: 'Ocurrió un inconveniente al registrar la solicitud. Por favor intenta nuevamente más tarde.',
          confirmButtonColor: '#6A5ACD'
        });
      }
    });
  }
}
