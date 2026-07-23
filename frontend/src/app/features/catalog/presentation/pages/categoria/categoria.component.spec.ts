import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoriaComponent } from './categoria.component';
import { CATEGORIA_REPOSITORY, CategoriaRepository } from '../../../domain/repositories/categoria.repository';
import { of } from 'rxjs';
import { Categoria } from '../../../domain/models/categoria.model';

describe('CategoriaComponent', () => {
  let component: CategoriaComponent;
  let fixture: ComponentFixture<CategoriaComponent>;
  let mockRepository: jasmine.SpyObj<CategoriaRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<CategoriaRepository>('CategoriaRepository', [
      'listarCategorias',
      'obtenerPorId',
      'registrarCategoria',
      'editarCategoria',
      'eliminarCategoria',
      'buscarPorNombre'
    ]);

    // Configurar valores por defecto para los mocks
    mockRepository.listarCategorias.and.returnValue(of([
      new Categoria(1, 'Categoría 1'),
      new Categoria(2, 'Categoría 2')
    ]));

    await TestBed.configureTestingModule({
      imports: [CategoriaComponent],
      providers: [
        { provide: CATEGORIA_REPOSITORY, useValue: mockRepository }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load categories on init', () => {
    expect(mockRepository.listarCategorias).toHaveBeenCalled();
    expect(component.categorias().length).toBe(2);
    expect(component.categorias()[0].nombre).toBe('Categoría 1');
  });

  it('should register a new category successfully', () => {
    const newCat = new Categoria(3, 'Categoría Nueva');
    mockRepository.registrarCategoria.and.returnValue(of(newCat));
    mockRepository.listarCategorias.and.returnValue(of([
      new Categoria(1, 'Categoría 1'),
      new Categoria(2, 'Categoría 2'),
      newCat
    ]));

    component.mostrarDialogo();
    component.form.patchValue({ nombre: 'Categoría Nueva' });
    
    component.guardar();
    
    expect(mockRepository.registrarCategoria).toHaveBeenCalledWith('Categoría Nueva');
    expect(component.mostrarFormulario()).toBeFalse();
  });

  it('should edit an existing category successfully', () => {
    const updatedCat = new Categoria(1, 'Categoría Modificada');
    mockRepository.editarCategoria.and.returnValue(of(updatedCat));
    mockRepository.listarCategorias.and.returnValue(of([
      updatedCat,
      new Categoria(2, 'Categoría 2')
    ]));

    component.editar(new Categoria(1, 'Categoría 1'));
    component.form.patchValue({ nombre: 'Categoría Modificada' });
    
    component.guardar();
    
    expect(mockRepository.editarCategoria).toHaveBeenCalledWith(1, 'Categoría Modificada');
    expect(component.mostrarFormulario()).toBeFalse();
  });
});
