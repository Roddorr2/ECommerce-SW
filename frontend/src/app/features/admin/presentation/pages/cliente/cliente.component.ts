import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CLIENTE_REPOSITORY } from '../../../../sales/domain/repositories/cliente.repository';
import { Cliente } from '../../../../sales/domain/models/cliente.model';

@Component({
  selector: 'app-cliente',
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './cliente.component.html',
  styleUrl: './cliente.component.scss',
})
export class ClienteComponent implements OnInit {
  private clienteRepository = inject(CLIENTE_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  clientes = signal<Cliente[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    telefono: ['', [Validators.required, Validators.pattern('^\\+?[0-9]{7,15}$')]],
    direccion: ['', [Validators.required, Validators.maxLength(200)]],
  });

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.cargando.set(true);
    this.clienteRepository.listarClientes().subscribe({
      next: (data) => {
        this.clientes.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudo cargar la lista de clientes.');
      },
    });
  }

  editar(cliente: Cliente): void {
    this.form.patchValue({
      id: cliente.id,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
    });
    this.editando.set(true);
    this.mostrarDialog.set(true);
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);
    const { id, telefono, direccion } = this.form.value;
    const oldClient = this.clientes().find(c => c.id === id);
    const payload = {
      id,
      usuarioId: oldClient ? oldClient.usuarioId : 0,
      correo: oldClient ? oldClient.correo : '',
      telefono,
      direccion
    };
    this.clienteRepository.editarCliente(payload).subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Cliente actualizado',
          detail: 'Datos actualizados correctamente.',
          life: 3000,
        });
        this.cargarClientes();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo actualizar el cliente.');
      },
    });
  }

  confirmarEliminar(cliente: Cliente): void {
    this.confirmationService.confirm({
      message: `¿Eliminar al cliente "${cliente.usuario}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(cliente.id),
    });
  }

  eliminar(id: number): void {
    this.clienteRepository.eliminarCliente(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Cliente eliminado correctamente.',
          life: 3000,
        });
        this.cargarClientes();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo eliminar el cliente.'),
    });
  }

  cancelar(): void {
    this.form.reset();
    this.editando.set(false);
    this.mostrarDialog.set(false);
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
