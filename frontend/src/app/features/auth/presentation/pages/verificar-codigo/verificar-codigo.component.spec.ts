import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VerificarCodigoComponent } from './verificar-codigo.component';
import { provideRouter } from '@angular/router';
import { AUTH_REPOSITORY, AuthRepository } from '../../../domain/repositories/auth.repository';

describe('VerificarCodigoComponent', () => {
  let component: VerificarCodigoComponent;
  let fixture: ComponentFixture<VerificarCodigoComponent>;
  let mockAuthRepo: jasmine.SpyObj<AuthRepository>;

  beforeEach(async () => {
    mockAuthRepo = jasmine.createSpyObj<AuthRepository>('AuthRepository', [
      'login',
      'verificarCodigo',
      'reenviarCodigo',
      'registrarCliente',
      'solicitarRecuperacion',
      'confirmarRecuperacion',
      'cambiarContrasena',
      'guardarSesion',
      'guardarCorreoPendiente',
      'obtenerCorreoPendiente',
      'limpiarCorreoPendiente',
      'obtenerToken',
      'obtenerRol',
      'estaAutenticado',
      'cerrarSesion'
    ]);

    mockAuthRepo.obtenerCorreoPendiente.and.returnValue('test@example.com');

    await TestBed.configureTestingModule({
      imports: [VerificarCodigoComponent],
      providers: [
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerificarCodigoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
