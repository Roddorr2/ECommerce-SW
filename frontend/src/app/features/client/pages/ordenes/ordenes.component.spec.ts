import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrdenesClienteComponent } from './ordenes.component';
import { ORDEN_REPOSITORY, OrdenRepository } from '../../../sales/domain/repositories/orden.repository';
import { of } from 'rxjs';

describe('OrdenesClienteComponent', () => {
  let component: OrdenesClienteComponent;
  let fixture: ComponentFixture<OrdenesClienteComponent>;
  let mockRepository: jasmine.SpyObj<OrdenRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<OrdenRepository>('OrdenRepository', [
      'obtenerMisOrdenes', 'obtenerPorId', 'crearOrdenDesdeCarrito', 'listarOrdenes', 'obtenerPorCliente', 'obtenerPorEstado', 'cambiarEstado', 'cancelarOrden'
    ]);

    mockRepository.obtenerMisOrdenes.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [OrdenesClienteComponent],
      providers: [
        { provide: ORDEN_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdenesClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
