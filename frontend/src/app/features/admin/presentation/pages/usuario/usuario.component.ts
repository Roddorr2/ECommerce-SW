import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { USUARIO_REPOSITORY } from '../../../domain/repositories/usuario.repository';
import { ROL_REPOSITORY } from '../../../domain/repositories/rol.repository';
import { Usuario } from '../../../domain/models/usuario.model';
import { Rol } from '../../../domain/models/rol.model';

@Component({
  selector: 'app-usuario',
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    SelectModule,
    PasswordModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.scss',
})
export class UsuarioComponent implements OnInit {
  private usuarioRepository = inject(USUARIO_REPOSITORY);
  private rolRepository = inject(ROL_REPOSITORY);
  private fb = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  estadoOptions = [
    { label: 'Activo', value: true },
    { label: 'Inactivo', value: false },
  ];

  usuarios = signal<Usuario[]>([]);
  roles = signal<Rol[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  editando = signal(false);
  mostrarDialog = signal(false);

  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    correo: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
    contrasena: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(256)]],
    activo: [true, Validators.required],
    rolId: [null, Validators.required],
  });

  ngOnInit(): void {
    this.cargarUsuarios();
    this.cargarRoles();
  }

  cargarUsuarios(): void {
    this.cargando.set(true);
    this.usuarioRepository.listarUsuarios().subscribe({
      next: (data) => {
        this.usuarios.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar los usuarios.');
      },
    });
  }

  cargarRoles(): void {
    this.rolRepository.listarRoles().subscribe({
      next: (data) => this.roles.set(data),
      error: () => this.mostrarError('No se pudieron cargar los roles.'),
    });
  }

  mostrarDialogo(): void {
    this.editando.set(false);
    this.form.reset({ activo: true });
    this.form.get('contrasena')?.setValidators([Validators.required, Validators.minLength(8)]);
    this.form.get('contrasena')?.updateValueAndValidity();
    this.mostrarDialog.set(true);
  }

  editar(usuario: Usuario): void {
    const rol = this.roles().find((r) => r.nombre === usuario.rol);
    this.form.patchValue({
      id: usuario.id,
      nombre: usuario.nombre,
      correo: usuario.email,
      contrasena: '',
      activo: usuario.estado,
      rolId: rol?.id ?? null,
    });
    this.form.get('contrasena')?.clearValidators();
    this.form.get('contrasena')?.updateValueAndValidity();
    this.editando.set(true);
    this.mostrarDialog.set(true);
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);
    const { id, nombre, correo, contrasena, activo, rolId } = this.form.value;
    const obs = this.editando()
      ? this.usuarioRepository.editarUsuario(id, nombre, correo, activo, rolId)
      : this.usuarioRepository.registrarUsuario(nombre, correo, contrasena, activo, rolId);
    obs.subscribe({
      next: () => {
        this.guardando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: this.editando() ? 'Usuario actualizado' : 'Usuario registrado',
          detail: 'Operación exitosa.',
          life: 3000,
        });
        this.cargarUsuarios();
        this.cancelar();
      },
      error: (err) => {
        this.guardando.set(false);
        this.mostrarError(err.error?.mensaje || 'No se pudo guardar el usuario.');
      },
    });
  }

  confirmarEliminar(usuario: Usuario): void {
    this.confirmationService.confirm({
      message: `¿Eliminar al usuario "${usuario.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(usuario.id),
    });
  }

  eliminar(id: number): void {
    this.usuarioRepository.eliminarUsuario(id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Eliminado',
          detail: 'Usuario eliminado correctamente.',
          life: 3000,
        });
        this.cargarUsuarios();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo eliminar the usuario.'),
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
