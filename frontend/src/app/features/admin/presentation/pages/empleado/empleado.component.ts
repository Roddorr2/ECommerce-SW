import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { EMPLEADO_REPOSITORY } from '../../../domain/repositories/empleado.repository';
import { USUARIO_REPOSITORY } from '../../../domain/repositories/usuario.repository';
import { AREA_REPOSITORY } from '../../../domain/repositories/area.repository';
import { CARGO_REPOSITORY } from '../../../domain/repositories/cargo.repository';
import { Empleado } from '../../../domain/models/empleado.model';
import { Usuario } from '../../../domain/models/usuario.model';
import { Area } from '../../../domain/models/area.model';
import { Cargo } from '../../../domain/models/cargo.model';

@Component({
  selector: 'app-empleado',
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    SelectModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './empleado.component.html',
  styleUrl: './empleado.component.scss',
})
export class EmpleadoComponent implements OnInit {
  private empleadoRepository = inject(EMPLEADO_REPOSITORY);
  private usuarioRepository = inject(USUARIO_REPOSITORY);
  private areaRepository = inject(AREA_REPOSITORY);
  private cargoRepository = inject(CARGO_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  empleados = signal<Empleado[]>([]);
  usuarios = signal<Usuario[]>([]);
  areas = signal<Area[]>([]);
  cargos = signal<Cargo[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    usuarioId: [null, Validators.required],
    areaId: [null, Validators.required],
    cargoId: [null, Validators.required],
  });

  ngOnInit(): void {
    this.cargarEmpleados();
    this.cargarUsuarios();
    this.cargarAreas();
    this.cargarCargos();
  }

  cargarEmpleados(): void {
    this.cargando.set(true);
    this.empleadoRepository.listarEmpleados().subscribe({
      next: (data) => {
        this.empleados.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los empleados.');
      },
    });
  }

  cargarUsuarios(): void {
    this.usuarioRepository.listarUsuarios().subscribe({
      next: (data) => this.usuarios.set(data),
      error: () => this.mostrarError('No se pudieron cargar los usuarios.'),
    });
  }

  cargarAreas(): void {
    this.areaRepository.listarAreas().subscribe({
      next: (data) => this.areas.set(data),
      error: () => this.mostrarError('No se pudieron cargar las áreas.'),
    });
  }

  cargarCargos(): void {
    this.cargoRepository.listarCargos().subscribe({
      next: (data) => this.cargos.set(data),
      error: () => this.mostrarError('No se pudieron cargar los cargos.'),
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset();
    this.mostrarDialog.set(true);
  }

  editar(empleado: Empleado): void {
    const area = this.areas().find((a) => a.nombre === empleado.area);
    const cargo = this.cargos().find((c) => c.nombre === empleado.cargo);
    this.form.patchValue({
      id: empleado.id,
      usuarioId: empleado.usuarioId,
      areaId: area?.id ?? null,
      cargoId: cargo?.id ?? null,
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
    const { id, usuarioId, areaId, cargoId } = this.form.value;
    const obs = this.editando()
      ? this.empleadoRepository.editarEmpleado(id, usuarioId, areaId, cargoId)
      : this.empleadoRepository.registrarEmpleado(usuarioId, areaId, cargoId);
    obs.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Empleado actualizado' : 'Empleado registrado',
          detail: 'Operación exitosa.',
          life: 3000,
        });
        this.cargarEmpleados();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el empleado.');
      },
    });
  }

  confirmarEliminar(empleado: Empleado): void {
    this.confirmationService.confirm({
      message: `¿Eliminar al empleado "${empleado.nombreUsuario}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(empleado.id),
    });
  }

  eliminar(id: number): void {
    this.empleadoRepository.eliminarEmpleado(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Empleado eliminado correctamente.',
          life: 3000,
        });
        this.cargarEmpleados();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo eliminar el empleado.'),
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
