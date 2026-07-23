import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { provideRouter } from '@angular/router';
import { LucideAngularModule, User, Lock, LogIn } from 'lucide-angular';
import { AUTH_REPOSITORY, AuthRepository } from '../../../domain/repositories/auth.repository';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
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
      imports: [
        LoginComponent,
        LucideAngularModule.pick({ User, Lock, LogIn })
      ],
      providers: [
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
