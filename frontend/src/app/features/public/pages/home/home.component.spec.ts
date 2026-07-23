import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { PRODUCTO_REPOSITORY, ProductoRepository } from '../../../catalog/domain/repositories/producto.repository';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { AUTH_REPOSITORY, AuthRepository } from '../../../auth/domain/repositories/auth.repository';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let mockProductoRepo: jasmine.SpyObj<ProductoRepository>;
  let mockAuthRepo: jasmine.SpyObj<AuthRepository>;

  beforeEach(async () => {
    mockProductoRepo = jasmine.createSpyObj<ProductoRepository>('ProductoRepository', [
      'listarProductos', 'obtenerUrlImagen'
    ]);

    mockProductoRepo.listarProductos.and.returnValue(of([]));
    mockProductoRepo.obtenerUrlImagen.and.returnValue('url');

    mockAuthRepo = jasmine.createSpyObj<AuthRepository>('AuthRepository', [
      'obtenerToken', 'obtenerRol', 'cerrarSesion'
    ]);

    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [
        { provide: PRODUCTO_REPOSITORY, useValue: mockProductoRepo },
        { provide: AUTH_REPOSITORY, useValue: mockAuthRepo },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
