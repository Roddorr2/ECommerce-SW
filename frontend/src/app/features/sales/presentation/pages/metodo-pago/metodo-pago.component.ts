import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { METODO_PAGO_REPOSITORY } from '../../../domain/repositories/metodo-pago.repository';
import { MetodoPago } from '../../../domain/models/metodo-pago.model';

@Component({
  selector: 'app-metodo-pago',
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
  templateUrl: './metodo-pago.component.html',
  styleUrl: './metodo-pago.component.scss',
})
export class MetodoPagoComponent implements OnInit {
  private metodoPagoRepository = inject(METODO_PAGO_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  metodos = signal<MetodoPago[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarMetodos();
  }

  cargarMetodos(): void {
    this.cargando.set(true);
    this.metodoPagoRepository.listarMetodosPago().subscribe({
      next: (data) => {
        this.metodos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los métodos de pago.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(metodo: MetodoPago): void {
    this.form.patchValue(metodo);
    this.editando.set(true);
    this.mostrarDialog.set(true);
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const observable = this.editando()
      ? this.metodoPagoRepository.editarMetodoPago(this.form.value)
      : this.metodoPagoRepository.registrarMetodoPago(this.form.value);

    observable.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Método actualizado' : 'Método registrado',
          detail: `El método fue ${this.editando() ? 'actualizado' : 'creado'} correctamente.`,
          life: 3000,
        });
        this.cargarMetodos();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el método de pago.');
      },
    });
  }

  confirmarEliminar(metodo: MetodoPago): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar "${metodo.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(metodo.id),
    });
  }

  eliminar(id: number): void {
    this.metodoPagoRepository.eliminarMetodoPago(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Método de pago eliminado correctamente.',
          life: 3000,
        });
        this.cargarMetodos();
      },
      error: (err) =>
        this.mostrarError(
          err.error?.mensaje || 'No se pudo eliminar. Puede tener órdenes asociadas.',
        ),
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
