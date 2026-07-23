import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CLIENTE_REPOSITORY } from '../../../sales/domain/repositories/cliente.repository';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { Cliente } from '../../../sales/domain/models/cliente.model';

@Component({
  selector: 'app-direcciones',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, ToastModule, DialogModule],
  providers: [MessageService],
  templateUrl: './direcciones.component.html',
  styleUrl: './direcciones.component.scss'
})
export class DireccionesComponent implements OnInit {
  private clienteRepository = inject(CLIENTE_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  cargando = signal(true);
  guardando = signal(false);
  mostrarDialog = signal(false);
  cliente = signal<Cliente | null>(null);

  form: FormGroup = this.fb.group({
    direccion: ['', [Validators.required, Validators.maxLength(200)]]
  });

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.cargando.set(true);
    this.clienteRepository.obtenerPerfil().subscribe({
      next: (data) => {
        this.cliente.set(data);
        this.form.patchValue({ direccion: data.direccion });
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo cargar la información de tu dirección.'
        });
      }
    });
  }

  abrirEditar(): void {
    if (this.cliente()) {
      this.form.patchValue({ direccion: this.cliente()!.direccion });
    }
    this.mostrarDialog.set(true);
  }

  guardar(): void {
    if (this.form.invalid || !this.cliente()) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const nuevaDireccion = this.form.value.direccion;

    this.clienteRepository.actualizarPerfil({
      id: this.cliente()!.id,
      usuarioId: this.cliente()!.usuarioId,
      correo: this.cliente()!.correo,
      telefono: this.cliente()!.telefono,
      direccion: nuevaDireccion
    }).subscribe({
      next: (updated) => {
        this.guardando.set(false);
        this.cliente.set(updated);
        this.mostrarDialog.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Dirección actualizada',
          detail: 'Tu dirección de envío se guardó correctamente.'
        });
      },
      error: (err) => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo actualizar la dirección.'
        });
      }
    });
  }
}
