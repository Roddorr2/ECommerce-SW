import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RolComponent } from './rol.component';
import { ROL_REPOSITORY, RolRepository } from '../../../domain/repositories/rol.repository';
import { of } from 'rxjs';
import { Rol } from '../../../domain/models/rol.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('RolComponent', () => {
  let component: RolComponent;
  let fixture: ComponentFixture<RolComponent>;
  let mockRepository: jasmine.SpyObj<RolRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<RolRepository>('RolRepository', [
      'listarRoles',
      'obtenerPorId',
      'registrarRol',
      'editarRol',
      'eliminarRol',
      'buscarPorNombre'
    ]);

    mockRepository.listarRoles.and.returnValue(of([
      new Rol(1, 'Admin'),
      new Rol(2, 'Client')
    ]));

    await TestBed.configureTestingModule({
      imports: [RolComponent],
      providers: [
        { provide: ROL_REPOSITORY, useValue: mockRepository },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load roles on init', () => {
    expect(mockRepository.listarRoles).toHaveBeenCalled();
    expect(component.roles().length).toBe(2);
    expect(component.roles()[0].nombre).toBe('Admin');
  });
});
