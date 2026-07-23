import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MovimientoStockComponent } from './movimiento-stock.component';
import { MOVIMIENTO_STOCK_REPOSITORY, MovimientoStockRepository } from '../../../domain/repositories/movimiento-stock.repository';
import { of } from 'rxjs';
import { MovimientoStock } from '../../../domain/models/movimiento-stock.model';

describe('MovimientoStockComponent', () => {
  let component: MovimientoStockComponent;
  let fixture: ComponentFixture<MovimientoStockComponent>;
  let mockRepository: jasmine.SpyObj<MovimientoStockRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<MovimientoStockRepository>('MovimientoStockRepository', [
      'obtenerMovimientosPorProducto'
    ]);

    mockRepository.obtenerMovimientosPorProducto.and.returnValue(of([
      new MovimientoStock(1, 'Producto 1', 'SKU-001', 10, 15, 'ENTRADA', 'REF-001', 'Admin', '2026-06-04T12:00:00', 'Observacion')
    ]));

    await TestBed.configureTestingModule({
      imports: [MovimientoStockComponent],
      providers: [
        { provide: MOVIMIENTO_STOCK_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovimientoStockComponent);
    component = fixture.componentInstance;
    
    // Configurar input requerido
    fixture.componentRef.setInput('productoId', 1);
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load stock movements on init', () => {
    expect(mockRepository.obtenerMovimientosPorProducto).toHaveBeenCalledWith(1);
    expect(component.movimientos().length).toBe(1);
    expect(component.movimientos()[0].cantidadNueva).toBe(15);
  });
});
