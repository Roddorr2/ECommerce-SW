import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MetodoPagoComponent } from './metodo-pago.component';
import { METODO_PAGO_REPOSITORY, MetodoPagoRepository } from '../../../domain/repositories/metodo-pago.repository';
import { of } from 'rxjs';

describe('MetodoPagoComponent', () => {
  let component: MetodoPagoComponent;
  let fixture: ComponentFixture<MetodoPagoComponent>;
  let mockRepository: jasmine.SpyObj<MetodoPagoRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<MetodoPagoRepository>('MetodoPagoRepository', [
      'listarMetodosPago', 'obtenerPorId', 'registrarMetodoPago', 'editarMetodoPago', 'eliminarMetodoPago', 'buscarPorNombre'
    ]);

    mockRepository.listarMetodosPago.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [MetodoPagoComponent],
      providers: [
        { provide: METODO_PAGO_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetodoPagoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
