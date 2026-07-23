import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangePasswordComponent } from './change-password.component';
import { provideRouter } from '@angular/router';
import { AUTH_REPOSITORY, AuthRepository } from '../../../features/auth/domain/repositories/auth.repository';

describe('ChangePasswordComponent', () => {
  let component: ChangePasswordComponent;
  let fixture: ComponentFixture<ChangePasswordComponent>;
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

    await TestBed.configureTestingModule({
      imports: [ChangePasswordComponent],
      providers: [
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
