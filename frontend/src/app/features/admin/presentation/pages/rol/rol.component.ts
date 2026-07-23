import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ROL_REPOSITORY } from '../../../domain/repositories/rol.repository';
import { Rol } from '../../../domain/models/rol.model';

@Component({
  selector: 'app-rol',
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
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.scss',
})
export class RolComponent implements OnInit {
  private rolRepository = inject(ROL_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  roles = signal<Rol[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
  });

  ngOnInit(): void {
    this.cargarRoles();
  }

  cargarRoles(): void {
    this.cargando.set(true);
    this.rolRepository.listarRoles().subscribe({
      next: (data) => {
        this.roles.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los roles.');
      },
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(rol: Rol): void {
    this.form.patchValue(rol);
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
      ? this.rolRepository.editarRol(id, nombre)
      : this.rolRepository.registrarRol(nombre);
    obs.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Rol actualizado' : 'Rol registrado',
          detail: 'Operación exitosa.',
          life: 3000,
        });
        this.cargarRoles();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el rol.');
      },
    });
  }

  confirmarEliminar(rol: Rol): void {
    this.confirmationService.confirm({
      message: `¿Eliminar rol "${rol.nombre}"? Puede estar asociado a usuarios.`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(rol.id),
    });
  }

  eliminar(id: number): void {
    this.rolRepository.eliminarRol(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Rol eliminado correctamente.',
          life: 3000,
        });
        this.cargarRoles();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo eliminar. Puede estar asociado a usuarios.'),
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
