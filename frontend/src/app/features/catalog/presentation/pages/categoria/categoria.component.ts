import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { Categoria } from '../../../domain/models/categoria.model';
import { DialogModule } from 'primeng/dialog';
import { CATEGORIA_REPOSITORY } from '../../../domain/repositories/categoria.repository';

@Component({
  selector: 'app-categoria',
  imports: [
    DialogModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './categoria.component.html',
  styleUrl: './categoria.component.scss',
})
export class CategoriaComponent implements OnInit {
  private categoriaRepository = inject(CATEGORIA_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  categorias = signal<Categoria[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  mostrarFormulario = signal(false);
  editando = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.cargando.set(true);
    this.categoriaRepository.listarCategorias().subscribe({
      next: (data) => {
        this.categorias.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar las categorías.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarFormulario.set(true);
  }

  editar(categoria: Categoria): void {
    this.form.patchValue(categoria);
    this.editando.set(true);
    this.mostrarFormulario.set(true);
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const observable = this.editando()
      ? this.categoriaRepository.editarCategoria(this.form.value.id, this.form.value.nombre)
      : this.categoriaRepository.registrarCategoria(this.form.value.nombre);

    observable.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Categoría actualizada' : 'Categoría registrada',
          detail: `La categoría fue ${this.editando() ? 'actualizada' : 'creada'} correctamente.`,
          life: 3000,
        });
        this.cargarCategorias();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar la categoría.');
      },
    });
  }

  confirmarEliminar(categoria: Categoria): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar "${categoria.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(categoria.id),
    });
  }

  eliminar(id: number): void {
    this.categoriaRepository.eliminarCategoria(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminada',
          detail: 'Categoría eliminada correctamente.',
          life: 3000,
        });
        this.cargarCategorias();
      },
      error: (err) => {
        this.mostrarError(
          err.error?.mensaje || 'No se pudo eliminar. Puede tener productos asociados.',
        );
      },
    });
  }

  cancelar(): void {
    this.form.reset();
    this.editando.set(false);
    this.mostrarFormulario.set(false);
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
