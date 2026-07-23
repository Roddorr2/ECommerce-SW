import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrdenesComponent } from './ordenes.component';
import { ORDEN_REPOSITORY, OrdenRepository } from '../../../domain/repositories/orden.repository';
import { of } from 'rxjs';

describe('OrdenesComponent', () => {
  let component: OrdenesComponent;
  let fixture: ComponentFixture<OrdenesComponent>;
  let mockRepository: jasmine.SpyObj<OrdenRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<OrdenRepository>('OrdenRepository', [
      'listarOrdenes', 'obtenerPorId', 'obtenerMisOrdenes', 'obtenerPorCliente', 'obtenerPorEstado', 'cambiarEstado', 'cancelarOrden'
    ]);

    mockRepository.listarOrdenes.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [OrdenesComponent],
      providers: [
        { provide: ORDEN_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrdenesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
