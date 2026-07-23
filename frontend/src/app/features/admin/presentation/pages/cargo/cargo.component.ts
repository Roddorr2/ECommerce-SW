import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CARGO_REPOSITORY } from '../../../domain/repositories/cargo.repository';
import { Cargo } from '../../../domain/models/cargo.model';

@Component({
  selector: 'app-cargo',
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
  templateUrl: './cargo.component.html',
  styleUrl: './cargo.component.scss',
})
export class CargoComponent implements OnInit {
  private cargoRepository = inject(CARGO_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  cargos = signal<Cargo[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarCargos();
  }

  cargarCargos(): void {
    this.cargando.set(true);
    this.cargoRepository.listarCargos().subscribe({
      next: (data) => {
        this.cargos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los cargos.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(cargo: Cargo): void {
    this.form.patchValue(cargo);
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
      ? this.cargoRepository.editarCargo(id, nombre)
      : this.cargoRepository.registrarCargo(nombre);
    obs.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Cargo actualizado' : 'Cargo registrado',
          detail: 'Operación exitosa.',
          life: 3000,
        });
        this.cargarCargos();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar.');
      },
    });
  }

  confirmarEliminar(cargo: Cargo): void {
    this.confirmationService.confirm({
      message: `¿Eliminar "${cargo.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(cargo.id),
    });
  }

  eliminar(id: number): void {
    this.cargoRepository.eliminarCargo(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminada',
          detail: 'Cargo eliminado.',
          life: 3000,
        });
        this.cargarCargos();
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
