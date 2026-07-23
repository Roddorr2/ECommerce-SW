import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProveedorComponent } from './proveedor.component';
import { PROVEEDOR_REPOSITORY, ProveedorRepository } from '../../../domain/repositories/proveedor.repository';
import { TIPO_PROVEEDOR_REPOSITORY, TipoProveedorRepository } from '../../../domain/repositories/tipo-proveedor.repository';
import { of } from 'rxjs';

describe('ProveedorComponent', () => {
  let component: ProveedorComponent;
  let fixture: ComponentFixture<ProveedorComponent>;
  let mockProveedorRepo: jasmine.SpyObj<ProveedorRepository>;
  let mockTipoProveedorRepo: jasmine.SpyObj<TipoProveedorRepository>;

  beforeEach(async () => {
    mockProveedorRepo = jasmine.createSpyObj<ProveedorRepository>('ProveedorRepository', [
      'listarProveedores', 'obtenerPorId', 'registrarProveedor', 'editarProveedor', 'eliminarProveedor', 'buscarPorTipoProveedor'
    ]);
    mockTipoProveedorRepo = jasmine.createSpyObj<TipoProveedorRepository>('TipoProveedorRepository', [
      'listarTiposProveedor'
    ]);

    mockProveedorRepo.listarProveedores.and.returnValue(of([]));
    mockTipoProveedorRepo.listarTiposProveedor.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [ProveedorComponent],
      providers: [
        { provide: PROVEEDOR_REPOSITORY, useValue: mockProveedorRepo },
        { provide: TIPO_PROVEEDOR_REPOSITORY, useValue: mockTipoProveedorRepo }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProveedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
