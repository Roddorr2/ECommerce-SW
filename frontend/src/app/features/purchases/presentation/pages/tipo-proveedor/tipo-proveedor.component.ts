import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { TIPO_PROVEEDOR_REPOSITORY } from '../../../domain/repositories/tipo-proveedor.repository';
import { TipoProveedor } from '../../../domain/models/tipo-proveedor.model';

@Component({
  selector: 'app-tipo-proveedor',
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
  templateUrl: './tipo-proveedor.component.html',
  styleUrl: './tipo-proveedor.component.scss',
})
export class TipoProveedorComponent implements OnInit {
  private tipoProveedorRepository = inject(TIPO_PROVEEDOR_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  tipos = signal<TipoProveedor[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos(): void {
    this.cargando.set(true);
    this.tipoProveedorRepository.listarTiposProveedor().subscribe({
      next: (data) => {
        this.tipos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los tipos de proveedor.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(tipo: TipoProveedor): void {
    this.form.patchValue(tipo);
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
      ? this.tipoProveedorRepository.editarTipoProveedor(this.form.value)
      : this.tipoProveedorRepository.registrarTipoProveedor(this.form.value);

    observable.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Tipo actualizado' : 'Tipo registrado',
          detail: `El tipo fue ${this.editando() ? 'actualizado' : 'creado'} correctamente.`,
          life: 3000,
        });
        this.cargarTipos();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el tipo de proveedor.');
      },
    });
  }

  confirmarEliminar(tipo: TipoProveedor): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar "${tipo.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(tipo.id),
    });
  }

  eliminar(id: number): void {
    this.tipoProveedorRepository.eliminarTipoProveedor(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Tipo de proveedor eliminado correctamente.',
          life: 3000,
        });
        this.cargarTipos();
      },
      error: (err) =>
        this.mostrarError(
          err.error?.mensaje || 'No se pudo eliminar. Puede tener proveedores asociados.',
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
