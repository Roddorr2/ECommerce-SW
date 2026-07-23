import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TipoProveedorComponent } from './tipo-proveedor.component';
import { TIPO_PROVEEDOR_REPOSITORY, TipoProveedorRepository } from '../../../domain/repositories/tipo-proveedor.repository';
import { of } from 'rxjs';

describe('TipoProveedorComponent', () => {
  let component: TipoProveedorComponent;
  let fixture: ComponentFixture<TipoProveedorComponent>;
  let mockRepository: jasmine.SpyObj<TipoProveedorRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<TipoProveedorRepository>('TipoProveedorRepository', [
      'listarTiposProveedor', 'obtenerPorId', 'registrarTipoProveedor', 'editarTipoProveedor', 'eliminarTipoProveedor', 'buscarPorNombre'
    ]);

    mockRepository.listarTiposProveedor.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [TipoProveedorComponent],
      providers: [
        { provide: TIPO_PROVEEDOR_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TipoProveedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
