import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductosClienteComponent } from './productos.component';
import { PRODUCTO_REPOSITORY, ProductoRepository } from '../../../catalog/domain/repositories/producto.repository';
import { CARRITO_REPOSITORY, CarritoRepository } from '../../../sales/domain/repositories/carrito.repository';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('ProductosClienteComponent', () => {
  let component: ProductosClienteComponent;
  let fixture: ComponentFixture<ProductosClienteComponent>;
  let mockProductoRepo: jasmine.SpyObj<ProductoRepository>;
  let mockCarritoRepo: jasmine.SpyObj<CarritoRepository>;

  beforeEach(async () => {
    mockProductoRepo = jasmine.createSpyObj<ProductoRepository>('ProductoRepository', [
      'listarProductos', 'obtenerUrlImagen'
    ]);
    mockCarritoRepo = jasmine.createSpyObj<CarritoRepository>('CarritoRepository', [
      'agregarItem'
    ]);

    mockProductoRepo.listarProductos.and.returnValue(of([]));
    mockProductoRepo.obtenerUrlImagen.and.returnValue('url');

    await TestBed.configureTestingModule({
      imports: [ProductosClienteComponent],
      providers: [
        { provide: PRODUCTO_REPOSITORY, useValue: mockProductoRepo },
        { provide: CARRITO_REPOSITORY, useValue: mockCarritoRepo },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductosClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
