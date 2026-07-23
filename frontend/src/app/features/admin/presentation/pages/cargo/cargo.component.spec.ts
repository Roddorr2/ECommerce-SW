import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CargoComponent } from './cargo.component';
import { CARGO_REPOSITORY, CargoRepository } from '../../../domain/repositories/cargo.repository';
import { of } from 'rxjs';
import { Cargo } from '../../../domain/models/cargo.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('CargoComponent', () => {
  let component: CargoComponent;
  let fixture: ComponentFixture<CargoComponent>;
  let mockRepository: jasmine.SpyObj<CargoRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<CargoRepository>('CargoRepository', [
      'listarCargos',
      'obtenerPorId',
      'registrarCargo',
      'editarCargo',
      'eliminarCargo'
    ]);

    mockRepository.listarCargos.and.returnValue(of([
      new Cargo(1, 'Gerente'),
      new Cargo(2, 'Desarrollador')
    ]));

    await TestBed.configureTestingModule({
      imports: [CargoComponent],
      providers: [
        { provide: CARGO_REPOSITORY, useValue: mockRepository },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CargoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load cargos on init', () => {
    expect(mockRepository.listarCargos).toHaveBeenCalled();
    expect(component.cargos().length).toBe(2);
    expect(component.cargos()[0].nombre).toBe('Gerente');
  });
});
