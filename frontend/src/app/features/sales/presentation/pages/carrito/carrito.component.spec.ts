import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CarritoComponent } from './carrito.component';
import { CARRITO_REPOSITORY, CarritoRepository } from '../../../domain/repositories/carrito.repository';
import { METODO_PAGO_REPOSITORY, MetodoPagoRepository } from '../../../domain/repositories/metodo-pago.repository';
import { ORDEN_REPOSITORY, OrdenRepository } from '../../../domain/repositories/orden.repository';
import { of } from 'rxjs';
import { Carrito } from '../../../domain/models/carrito.model';

import { provideRouter } from '@angular/router';

describe('CarritoComponent', () => {
  let component: CarritoComponent;
  let fixture: ComponentFixture<CarritoComponent>;
  let mockCarritoRepo: jasmine.SpyObj<CarritoRepository>;
  let mockMetodoPagoRepo: jasmine.SpyObj<MetodoPagoRepository>;
  let mockOrdenRepo: jasmine.SpyObj<OrdenRepository>;

  beforeEach(async () => {
    mockCarritoRepo = jasmine.createSpyObj<CarritoRepository>('CarritoRepository', [
      'obtenerCarritoActivo', 'agregarItem', 'actualizarItem', 'eliminarItem', 'vaciarCarrito'
    ]);
    mockMetodoPagoRepo = jasmine.createSpyObj<MetodoPagoRepository>('MetodoPagoRepository', [
      'listarMetodosPago'
    ]);
    mockOrdenRepo = jasmine.createSpyObj<OrdenRepository>('OrdenRepository', [
      'crearOrdenDesdeCarrito'
    ]);

    mockCarritoRepo.obtenerCarritoActivo.and.returnValue(of(new Carrito(1, [], 0)));
    mockMetodoPagoRepo.listarMetodosPago.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [CarritoComponent],
      providers: [
        provideRouter([]),
        { provide: CARRITO_REPOSITORY, useValue: mockCarritoRepo },
        { provide: METODO_PAGO_REPOSITORY, useValue: mockMetodoPagoRepo },
        { provide: ORDEN_REPOSITORY, useValue: mockOrdenRepo }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarritoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
