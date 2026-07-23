import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardClientComponent } from './dashboard-client.component';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AUTH_REPOSITORY, AuthRepository } from '../../../auth/domain/repositories/auth.repository';

describe('DashboardClientComponent', () => {
  let component: DashboardClientComponent;
  let fixture: ComponentFixture<DashboardClientComponent>;
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
      imports: [DashboardClientComponent],
      providers: [
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([]),
        provideAnimations()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
