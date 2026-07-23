import { Component, inject, OnInit, signal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PRODUCTO_REPOSITORY } from '../../../domain/repositories/producto.repository';
import { CATEGORIA_REPOSITORY } from '../../../domain/repositories/categoria.repository';
import { Producto } from '../../../domain/models/producto.model';
import { Categoria } from '../../../domain/models/categoria.model';
import { firstValueFrom } from 'rxjs';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-producto',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    TextareaModule,
    SelectModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    CurrencyPipe,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './producto.component.html',
  styleUrl: './producto.component.scss',
})
export class ProductoComponent implements OnInit {
  private productoRepository = inject(PRODUCTO_REPOSITORY);
  private categoriaRepository = inject(CATEGORIA_REPOSITORY);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private fb = inject(FormBuilder);

  productos = signal<Producto[]>([]);
  categorias = signal<Categoria[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  mostrarFormulario = signal(false);
  editando = signal(false);
  imagenPreview = signal<string | null>(null);
  busqueda = '';
  imagenArchivo?: File;

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
    sku: ['', [Validators.required, Validators.maxLength(50)]],
    descripcion: ['', Validators.maxLength(500)],
    precio: [null, [Validators.required, Validators.min(0.01)]],
    imagenNombre: [''],
    activo: [true],
    categoriaId: [null, Validators.required],
  });

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
  }

  cargarProductos(): void {
    this.cargando.set(true);
    this.productoRepository.listarProductos().subscribe({
      next: (data) => {
        this.productos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los productos.');
      },
    });
  }

  cargarCategorias(): void {
    this.categoriaRepository.listarCategorias().subscribe({
      next: (data) => this.categorias.set(data),
      error: () => this.mostrarError('No se pudieron cargar las categorías.'),
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.imagenArchivo = undefined;
    this.imagenPreview.set(null);
    this.form.reset({
      id: null,
      nombre: '',
      sku: '',
      descripcion: '',
      precio: null,
      imagenNombre: '',
      activo: true,
      categoriaId: null,
    });
    this.mostrarFormulario.set(true);
  }

  editar(producto: Producto): void {
    this.form.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      sku: producto.sku,
      descripcion: producto.descripcion,
      precio: producto.precio,
      imagenNombre: producto.imagenNombre ?? '',
      activo: producto.activo,
      categoriaId: producto.categoriaId,
    });
    this.imagenArchivo = undefined;
    this.imagenPreview.set(
      producto.imagenNombre ? this.productoRepository.obtenerUrlImagen(producto.imagenNombre) : null,
    );
    this.editando.set(true);
    this.mostrarFormulario.set(true);
  }

  async guardar(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const valores = this.form.value;

    try {
      if (this.imagenArchivo) {
        const imagenResp = await firstValueFrom(
          this.productoRepository.subirImagen(this.imagenArchivo),
        );
        valores.imagenNombre = imagenResp.nombreArchivo;
      }

      const observable = this.editando()
        ? this.productoRepository.editarProducto(valores)
        : this.productoRepository.registrarProducto(valores);

      await firstValueFrom(observable);

      this.guardando.set(false);
      this.messageService.add({
        severity: 'success',
        summary: this.editando() ? 'Producto actualizado' : 'Producto registrado',
        detail: `El producto fue ${this.editando() ? 'actualizado' : 'creado'} correctamente.`,
        life: 3000,
      });

      this.cargarProductos();
      this.cancelar();
    } catch (err: unknown) {
      this.guardando.set(false);
      const error = err as { error?: { mensaje?: string } };
      this.mostrarError(error.error?.mensaje || 'Ocurrió un error al guardar el producto.');
    }
  }

  confirmarEliminar(producto: Producto): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar "${producto.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminarProducto(producto.id),
    });
  }

  eliminarProducto(id: number): void {
    this.productoRepository.eliminarProducto(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Producto eliminado correctamente.',
          life: 3000,
        });
        this.cargarProductos();
      },
      error: () => this.mostrarError('No se pudo eliminar el producto.'),
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const tiposPermitidos = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp', 'image/gif'];

    if (!tiposPermitidos.includes(file.type)) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Formato no válido',
        detail: 'Solo JPG, PNG, WEBP, GIF.',
        life: 3000,
      });
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Archivo muy grande',
        detail: 'Máximo 5MB.',
        life: 3000,
      });
      return;
    }

    this.imagenArchivo = file;
    this.imagenPreview.set(URL.createObjectURL(file));
  }

  buscar(): void {
    if (!this.busqueda.trim()) {
      this.cargarProductos();
      return;
    }
    this.productoRepository.buscarProductosDisponibles(this.busqueda).subscribe({
      next: (data) => this.productos.set(data),
      error: () => this.mostrarError('Error al buscar productos.'),
    });
  }

  cancelar(): void {
    this.form.reset();
    this.imagenArchivo = undefined;
    this.imagenPreview.set(null);
    this.editando.set(false);
    this.mostrarFormulario.set(false);
  }

  obtenerUrlImagen(imagenNombre: string): string {
    return this.productoRepository.obtenerUrlImagen(imagenNombre);
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
