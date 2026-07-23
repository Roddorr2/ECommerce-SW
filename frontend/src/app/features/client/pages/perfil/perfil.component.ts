import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CLIENTE_REPOSITORY } from '../../../sales/domain/repositories/cliente.repository';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { Cliente } from '../../../sales/domain/models/cliente.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, ToastModule],
  providers: [MessageService],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent implements OnInit {
  private clienteRepository = inject(CLIENTE_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);

  cargando = signal(true);
  guardando = signal(false);
  cliente = signal<Cliente | null>(null);

  form: FormGroup = this.fb.group({
    id: [null],
    usuarioId: [null],
    nombre: [{ value: '', disabled: true }],
    correo: [{ value: '', disabled: true }],
    telefono: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{7,15}$/)]],
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
        this.form.patchValue({
          id: data.id,
          usuarioId: data.usuarioId,
          nombre: data.usuario,
          correo: data.correo,
          telefono: data.telefono,
          direccion: data.direccion
        });
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo cargar la información de tu perfil.'
        });
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const formVal = this.form.getRawValue();
    this.clienteRepository.actualizarPerfil({
      id: formVal.id,
      usuarioId: formVal.usuarioId,
      correo: formVal.correo,
      telefono: formVal.telefono,
      direccion: formVal.direccion
    }).subscribe({
      next: (updated) => {
        this.guardando.set(false);
        this.cliente.set(updated);
        this.messageService.add({
          severity: 'success',
          summary: 'Perfil actualizado',
          detail: 'Tus datos se guardaron correctamente.'
        });
      },
      error: (err) => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.mensaje || 'No se pudo actualizar el perfil.'
        });
      }
    });
  }
}
