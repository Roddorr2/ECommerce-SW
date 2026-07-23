import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UsuarioComponent } from './usuario.component';
import { USUARIO_REPOSITORY, UsuarioRepository } from '../../../domain/repositories/usuario.repository';
import { ROL_REPOSITORY, RolRepository } from '../../../domain/repositories/rol.repository';
import { of } from 'rxjs';
import { Usuario } from '../../../domain/models/usuario.model';
import { Rol } from '../../../domain/models/rol.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('UsuarioComponent', () => {
  let component: UsuarioComponent;
  let fixture: ComponentFixture<UsuarioComponent>;
  let mockUsuarioRepo: jasmine.SpyObj<UsuarioRepository>;
  let mockRolRepo: jasmine.SpyObj<RolRepository>;

  beforeEach(async () => {
    mockUsuarioRepo = jasmine.createSpyObj<UsuarioRepository>('UsuarioRepository', [
      'listarUsuarios', 'obtenerPorId', 'registrarUsuario', 'editarUsuario', 'eliminarUsuario'
    ]);
    mockRolRepo = jasmine.createSpyObj<RolRepository>('RolRepository', [
      'listarRoles', 'obtenerPorId', 'registrarRol', 'editarRol', 'eliminarRol', 'buscarPorNombre'
    ]);

    mockUsuarioRepo.listarUsuarios.and.returnValue(of([
      new Usuario(1, 'Carlos', 'carlos@mail.com', true, 'Admin', false)
    ]));
    mockRolRepo.listarRoles.and.returnValue(of([
      new Rol(1, 'Admin'),
      new Rol(2, 'Client')
    ]));

    await TestBed.configureTestingModule({
      imports: [UsuarioComponent],
      providers: [
        { provide: USUARIO_REPOSITORY, useValue: mockUsuarioRepo },
        { provide: ROL_REPOSITORY, useValue: mockRolRepo },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load list of users', () => {
    expect(mockUsuarioRepo.listarUsuarios).toHaveBeenCalled();
    expect(component.usuarios().length).toBe(1);
    expect(component.usuarios()[0].nombre).toBe('Carlos');
  });
});
