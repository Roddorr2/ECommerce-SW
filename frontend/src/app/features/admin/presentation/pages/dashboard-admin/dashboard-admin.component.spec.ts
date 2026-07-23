import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardAdminComponent } from './dashboard-admin.component';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AUTH_REPOSITORY, AuthRepository } from '../../../../auth/domain/repositories/auth.repository';

describe('DashboardAdminComponent', () => {
  let component: DashboardAdminComponent;
  let fixture: ComponentFixture<DashboardAdminComponent>;
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
      imports: [DashboardAdminComponent],
      providers: [
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([]),
        provideAnimations()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
