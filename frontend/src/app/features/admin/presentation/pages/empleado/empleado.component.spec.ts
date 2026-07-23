import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmpleadoComponent } from './empleado.component';
import { EMPLEADO_REPOSITORY, EmpleadoRepository } from '../../../domain/repositories/empleado.repository';
import { USUARIO_REPOSITORY, UsuarioRepository } from '../../../domain/repositories/usuario.repository';
import { AREA_REPOSITORY, AreaRepository } from '../../../domain/repositories/area.repository';
import { CARGO_REPOSITORY, CargoRepository } from '../../../domain/repositories/cargo.repository';
import { of } from 'rxjs';
import { Empleado } from '../../../domain/models/empleado.model';
import { Usuario } from '../../../domain/models/usuario.model';
import { Area } from '../../../domain/models/area.model';
import { Cargo } from '../../../domain/models/cargo.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('EmpleadoComponent', () => {
  let component: EmpleadoComponent;
  let fixture: ComponentFixture<EmpleadoComponent>;
  let mockEmpleadoRepo: jasmine.SpyObj<EmpleadoRepository>;
  let mockUsuarioRepo: jasmine.SpyObj<UsuarioRepository>;
  let mockAreaRepo: jasmine.SpyObj<AreaRepository>;
  let mockCargoRepo: jasmine.SpyObj<CargoRepository>;

  beforeEach(async () => {
    mockEmpleadoRepo = jasmine.createSpyObj<EmpleadoRepository>('EmpleadoRepository', [
      'listarEmpleados', 'obtenerPorId', 'registrarEmpleado', 'editarEmpleado', 'eliminarEmpleado'
    ]);
    mockUsuarioRepo = jasmine.createSpyObj<UsuarioRepository>('UsuarioRepository', [
      'listarUsuarios', 'obtenerPorId', 'registrarUsuario', 'editarUsuario', 'eliminarUsuario'
    ]);
    mockAreaRepo = jasmine.createSpyObj<AreaRepository>('AreaRepository', [
      'listarAreas', 'obtenerPorId', 'registrarArea', 'editarArea', 'eliminarArea'
    ]);
    mockCargoRepo = jasmine.createSpyObj<CargoRepository>('CargoRepository', [
      'listarCargos', 'obtenerPorId', 'registrarCargo', 'editarCargo', 'eliminarCargo'
    ]);

    mockEmpleadoRepo.listarEmpleados.and.returnValue(of([
      new Empleado(1, 10, 'Carlos', 'carlos@mail.com', 'Sistemas', 'Gerente')
    ]));
    mockUsuarioRepo.listarUsuarios.and.returnValue(of([
      new Usuario(10, 'Carlos', 'carlos@mail.com', true, 'ADMIN', false)
    ]));
    mockAreaRepo.listarAreas.and.returnValue(of([
      new Area(1, 'Sistemas')
    ]));
    mockCargoRepo.listarCargos.and.returnValue(of([
      new Cargo(1, 'Gerente')
    ]));

    await TestBed.configureTestingModule({
      imports: [EmpleadoComponent],
      providers: [
        { provide: EMPLEADO_REPOSITORY, useValue: mockEmpleadoRepo },
        { provide: USUARIO_REPOSITORY, useValue: mockUsuarioRepo },
        { provide: AREA_REPOSITORY, useValue: mockAreaRepo },
        { provide: CARGO_REPOSITORY, useValue: mockCargoRepo },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpleadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load list of employees', () => {
    expect(mockEmpleadoRepo.listarEmpleados).toHaveBeenCalled();
    expect(component.empleados().length).toBe(1);
  });
});
