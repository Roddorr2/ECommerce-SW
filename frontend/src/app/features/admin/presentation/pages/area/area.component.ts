import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { AREA_REPOSITORY } from '../../../domain/repositories/area.repository';
import { Area } from '../../../domain/models/area.model';

@Component({
  selector: 'app-area',
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
  templateUrl: './area.component.html',
  styleUrl: './area.component.scss',
})
export class AreaComponent implements OnInit {
  private areaRepository = inject(AREA_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  areas = signal<Area[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarAreas();
  }

  cargarAreas(): void {
    this.cargando.set(true);
    this.areaRepository.listarAreas().subscribe({
      next: (data) => {
        this.areas.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar las áreas.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(area: Area): void {
    this.form.patchValue(area);
    this.editando.set(true);
    this.mostrarDialog.set(true);
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);
    const { id, nombre } = this.form.value;
    const obs = this.editando()
      ? this.areaRepository.editarArea(id, nombre)
      : this.areaRepository.registrarArea(nombre);
    obs.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Área actualizada' : 'Área registrada',
          detail: 'Operación exitosa.',
          life: 3000,
        });
        this.cargarAreas();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar.');
      },
    });
  }

  confirmarEliminar(area: Area): void {
    this.confirmationService.confirm({
      message: `¿Eliminar "${area.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(area.id),
    });
  }

  eliminar(id: number): void {
    this.areaRepository.eliminarArea(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminada',
          detail: 'Área eliminada.',
          life: 3000,
        });
        this.cargarAreas();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo eliminar.'),
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
