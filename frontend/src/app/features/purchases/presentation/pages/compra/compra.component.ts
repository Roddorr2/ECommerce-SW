import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { COMPRA_REPOSITORY } from '../../../domain/repositories/compra.repository';
import { PROVEEDOR_REPOSITORY } from '../../../domain/repositories/proveedor.repository';
import { EMPLEADO_REPOSITORY } from '../../../../admin/domain/repositories/empleado.repository';
import { PRODUCTO_REPOSITORY } from '../../../../catalog/domain/repositories/producto.repository';
import { EstadoCompra } from '../../../domain/models/estado-compra.enum';
import { Compra, CompraResumen } from '../../../domain/models/compra.model';
import { Proveedor } from '../../../domain/models/proveedor.model';
import { Empleado } from '../../../../admin/domain/models/empleado.model';
import { Producto } from '../../../../catalog/domain/models/producto.model';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-compra',
  imports: [
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    SelectModule,
    InputTextModule,
    InputNumberModule,
    DatePipe,
    CurrencyPipe,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './compra.component.html',
  styleUrl: './compra.component.scss',
})
export class CompraComponent implements OnInit {
  private compraRepository = inject(COMPRA_REPOSITORY);
  private proveedorRepository = inject(PROVEEDOR_REPOSITORY);
  private empleadoRepository = inject(EMPLEADO_REPOSITORY);
  private productoRepository = inject(PRODUCTO_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  protected readonly EstadoCompra = EstadoCompra;

  compras = signal<CompraResumen[]>([]);
  proveedores = signal<Proveedor[]>([]);
  empleados = signal<Empleado[]>([]);
  productos = signal<Producto[]>([]);
  compraSeleccionada = signal<Compra | null>(null);
  cargando = signal(true);
  guardando = signal(false);
  mostrarFormulario = signal(false);
  mostrarDetalle = signal(false);

  estadosOpciones = [
    { label: 'Pendiente', value: EstadoCompra.PENDIENTE },
    { label: 'Recibida', value: EstadoCompra.RECIBIDA },
    { label: 'Cancelada', value: EstadoCompra.CANCELADA },
  ];

  form: FormGroup = this.fb.group({
    proveedorId: [null, Validators.required],
    empleadoId: [null, Validators.required],
    items: this.fb.array([], Validators.required),
  });

  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  ngOnInit(): void {
    this.cargarCompras();
    this.cargarProveedores();
    this.cargarEmpleados();
    this.cargarProductos();
  }

  cargarCompras(): void {
    this.cargando.set(true);
    this.compraRepository.listarCompras().subscribe({
      next: (data) => {
        this.compras.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar las compras.');
      },
    });
  }

  cargarProveedores(): void {
    this.proveedorRepository.listarProveedores().subscribe({
      next: (data) => this.proveedores.set(data),
      error: () => this.mostrarError('No se pudieron cargar los proveedores.'),
    });
  }

  cargarEmpleados(): void {
    this.empleadoRepository.listarEmpleados().subscribe({
      next: (data) => this.empleados.set(data),
      error: () => this.mostrarError('No se pudieron cargar los empleados.'),
    });
  }

  cargarProductos(): void {
    this.productoRepository.listarProductos().subscribe({
      next: (data) => this.productos.set(data),
      error: () => this.mostrarError('No se pudieron cargar los productos.'),
    });
  }

  verDetalle(id: number): void {
    this.compraRepository.obtenerPorId(id).subscribe({
      next: (data) => {
        this.compraSeleccionada.set(data);
        this.mostrarDetalle.set(true);
      },
      error: () => this.mostrarError('No se pudo cargar el detalle de la compra.'),
    });
  }

  mostrarDialogo(): void {
    this.items.clear();
    this.form.reset({ proveedorId: null, empleadoId: null });
    this.agregarItem();
    this.mostrarFormulario.set(true);
  }

  agregarItem(): void {
    this.items.push(
      this.fb.group({
        productoId: [null, Validators.required],
        cantidad: [1, [Validators.required, Validators.min(1)]],
        precioUnitario: [null, [Validators.required, Validators.min(0.01)]],
      }),
    );
  }

  eliminarItem(index: number): void {
    this.items.removeAt(index);
  }

  async guardar(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);

    try {
      await firstValueFrom(this.compraRepository.crearCompra(this.form.value));
      this.guardando.set(false);
      this.messageService.add({
        severity: 'success',
        summary: 'Compra registrada',
        detail: 'La compra fue creada correctamente.',
        life: 3000,
      });
      this.cargarCompras();
      this.cancelar();
    } catch (err: unknown) {
      this.guardando.set(false);
      const error = err as { error?: { mensaje?: string } };
      this.mostrarError(error.error?.mensaje || 'Ocurrió un error al guardar la compra.');
    }
  }

  recibirCompra(id: number): void {
    this.confirmationService.confirm({
      message: '¿Confirma que recibió esta compra? Se actualizará el stock.',
      header: 'Confirmar recepción',
      icon: 'pi pi-check-circle',
      acceptLabel: 'Sí, recibir',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.compraRepository.recibirCompra(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Compra recibida',
              detail: 'Stock actualizado correctamente.',
              life: 3000,
            });
            this.cargarCompras();
          },
          error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo recibir la compra.'),
        });
      },
    });
  }

  cancelarCompra(id: number): void {
    this.confirmationService.confirm({
      message: '¿Está seguro de cancelar esta compra?',
      header: 'Confirmar cancelación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, cancelar',
      rejectLabel: 'No',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.compraRepository.cancelarCompra(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Compra cancelada',
              detail: 'La compra fue cancelada.',
              life: 3000,
            });
            this.cargarCompras();
          },
          error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo cancelar la compra.'),
        });
      },
    });
  }

  confirmarEliminar(id: number): void {
    this.confirmationService.confirm({
      message: '¿Está seguro de eliminar esta compra?',
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.compraRepository.eliminarCompra(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Eliminada',
              detail: 'Compra eliminada correctamente.',
              life: 3000,
            });
            this.cargarCompras();
          },
          error: (err) =>
            this.mostrarError(
              err.error?.mensaje || 'No se pudo eliminar. Puede tener detalles asociados.',
            ),
        });
      },
    });
  }

  getBadgeClass(estado: EstadoCompra): string {
    switch (estado) {
      case EstadoCompra.PENDIENTE:
        return 'badge bg-warning';
      case EstadoCompra.RECIBIDA:
        return 'badge bg-success';
      case EstadoCompra.CANCELADA:
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }

  cancelar(): void {
    this.items.clear();
    this.form.reset();
    this.mostrarFormulario.set(false);
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
