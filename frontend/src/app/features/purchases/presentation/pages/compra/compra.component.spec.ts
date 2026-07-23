import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CompraComponent } from './compra.component';
import { COMPRA_REPOSITORY, CompraRepository } from '../../../domain/repositories/compra.repository';
import { PROVEEDOR_REPOSITORY, ProveedorRepository } from '../../../domain/repositories/proveedor.repository';
import { of } from 'rxjs';

describe('CompraComponent', () => {
  let component: CompraComponent;
  let fixture: ComponentFixture<CompraComponent>;
  let mockCompraRepo: jasmine.SpyObj<CompraRepository>;
  let mockProveedorRepo: jasmine.SpyObj<ProveedorRepository>;

  beforeEach(async () => {
    mockCompraRepo = jasmine.createSpyObj<CompraRepository>('CompraRepository', [
      'listarCompras', 'obtenerPorId', 'crearCompra', 'recibirCompra', 'cancelarCompra', 'cambiarEstado', 'obtenerPorProveedor', 'obtenerPorEstado', 'eliminarCompra'
    ]);
    mockProveedorRepo = jasmine.createSpyObj<ProveedorRepository>('ProveedorRepository', [
      'listarProveedores'
    ]);

    mockCompraRepo.listarCompras.and.returnValue(of([]));
    mockProveedorRepo.listarProveedores.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [CompraComponent],
      providers: [
        { provide: COMPRA_REPOSITORY, useValue: mockCompraRepo },
        { provide: PROVEEDOR_REPOSITORY, useValue: mockProveedorRepo }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
