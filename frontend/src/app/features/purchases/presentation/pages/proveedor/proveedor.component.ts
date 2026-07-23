import { Component, inject, OnInit, signal } from '@angular/core';
import { PROVEEDOR_REPOSITORY } from '../../../domain/repositories/proveedor.repository';
import { TIPO_PROVEEDOR_REPOSITORY } from '../../../domain/repositories/tipo-proveedor.repository';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Proveedor } from '../../../domain/models/proveedor.model';
import { TipoProveedor } from '../../../domain/models/tipo-proveedor.model';

@Component({
  selector: 'app-proveedor',
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    SelectModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './proveedor.component.html',
  styleUrl: './proveedor.component.scss',
})
export class ProveedorComponent implements OnInit {
  private proveedorRepository = inject(PROVEEDOR_REPOSITORY);
  private tipoProveedorRepository = inject(TIPO_PROVEEDOR_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  proveedores = signal<Proveedor[]>([]);
  tiposProveedor = signal<TipoProveedor[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.maxLength(50)]],
    telefono: ['', [Validators.required, Validators.maxLength(20)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
    direccion: ['', Validators.maxLength(100)],
    tipoProveedorId: [null, Validators.required],
  });

  ngOnInit(): void {
    this.cargarProveedores();
    this.cargarTiposProveedor();
  }

  cargarProveedores(): void {
    this.cargando.set(true);
    this.proveedorRepository.listarProveedores().subscribe({
      next: (data) => {
        this.proveedores.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los proveedores.');
      },
    });
  }

  cargarTiposProveedor(): void {
    this.tipoProveedorRepository.listarTiposProveedor().subscribe({
      next: (data) => this.tiposProveedor.set(data),
      error: () => this.mostrarError('No se pudieron cargar los tipos de proveedor.'),
    });
  }

  abrirNuevo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(proveedor: Proveedor): void {
    const tipoId =
      this.tiposProveedor().find((t) => t.nombre === proveedor.tipoProveedor)?.id ?? null;
    this.form.patchValue({
      id: proveedor.id,
      nombre: proveedor.nombre,
      telefono: proveedor.telefono,
      correo: proveedor.email,
      direccion: proveedor.direccion,
      tipoProveedorId: tipoId,
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
    const observable = this.editando()
      ? this.proveedorRepository.editarProveedor(this.form.value)
      : this.proveedorRepository.registrarProveedor(this.form.value);

    observable.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'ProveedorComponent actualizado' : 'ProveedorComponent registrado',
          detail: `El proveedor fue ${this.editando() ? 'actualizado' : 'creado'} correctamente.`,
          life: 3000,
        });
        this.cargarProveedores();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el proveedor.');
      },
    });
  }

  confirmarEliminar(proveedor: Proveedor): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar "${proveedor.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(proveedor.id),
    });
  }

  eliminar(id: number): void {
    this.proveedorRepository.eliminarProveedor(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'ProveedorComponent eliminado correctamente.',
          life: 3000,
        });
        this.cargarProveedores();
      },
      error: (err) =>
        this.mostrarError(
          err.error?.mensaje || 'No se pudo eliminar. Puede tener compras asociadas.',
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
