import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AreaComponent } from './area.component';
import { AREA_REPOSITORY, AreaRepository } from '../../../domain/repositories/area.repository';
import { of } from 'rxjs';
import { Area } from '../../../domain/models/area.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('AreaComponent', () => {
  let component: AreaComponent;
  let fixture: ComponentFixture<AreaComponent>;
  let mockRepository: jasmine.SpyObj<AreaRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<AreaRepository>('AreaRepository', [
      'listarAreas',
      'obtenerPorId',
      'registrarArea',
      'editarArea',
      'eliminarArea'
    ]);

    mockRepository.listarAreas.and.returnValue(of([
      new Area(1, 'Sistemas'),
      new Area(2, 'Ventas')
    ]));

    await TestBed.configureTestingModule({
      imports: [AreaComponent],
      providers: [
        { provide: AREA_REPOSITORY, useValue: mockRepository },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load areas on init', () => {
    expect(mockRepository.listarAreas).toHaveBeenCalled();
    expect(component.areas().length).toBe(2);
    expect(component.areas()[0].nombre).toBe('Sistemas');
  });
});
