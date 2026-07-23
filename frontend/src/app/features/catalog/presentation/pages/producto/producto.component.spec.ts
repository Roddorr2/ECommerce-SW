import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductoComponent } from './producto.component';
import { PRODUCTO_REPOSITORY, ProductoRepository } from '../../../domain/repositories/producto.repository';
import { CATEGORIA_REPOSITORY, CategoriaRepository } from '../../../domain/repositories/categoria.repository';
import { of } from 'rxjs';
import { Producto } from '../../../domain/models/producto.model';
import { Categoria } from '../../../domain/models/categoria.model';

describe('ProductoComponent', () => {
  let component: ProductoComponent;
  let fixture: ComponentFixture<ProductoComponent>;
  let mockProductoRepo: jasmine.SpyObj<ProductoRepository>;
  let mockCategoriaRepo: jasmine.SpyObj<CategoriaRepository>;

  beforeEach(async () => {
    mockProductoRepo = jasmine.createSpyObj<ProductoRepository>('ProductoRepository', [
      'listarProductos',
      'obtenerPorId',
      'registrarProducto',
      'editarProducto',
      'eliminarProducto',
      'subirImagen',
      'buscarProductosDisponibles',
      'obtenerUrlImagen'
    ]);

    mockCategoriaRepo = jasmine.createSpyObj<CategoriaRepository>('CategoriaRepository', [
      'listarCategorias'
    ]);

    mockProductoRepo.listarProductos.and.returnValue(of([
      new Producto(1, 'Producto 1', 'SKU-001', 'Desc 1', 10.5, 100, 'img1.png', true, 1, 'Cat 1')
    ]));

    mockCategoriaRepo.listarCategorias.and.returnValue(of([
      new Categoria(1, 'Cat 1')
    ]));

    await TestBed.configureTestingModule({
      imports: [ProductoComponent],
      providers: [
        { provide: PRODUCTO_REPOSITORY, useValue: mockProductoRepo },
        { provide: CATEGORIA_REPOSITORY, useValue: mockCategoriaRepo }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load products and categories on init', () => {
    expect(mockProductoRepo.listarProductos).toHaveBeenCalled();
    expect(mockCategoriaRepo.listarCategorias).toHaveBeenCalled();
    expect(component.productos().length).toBe(1);
    expect(component.categorias().length).toBe(1);
  });
});
