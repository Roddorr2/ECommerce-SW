import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClienteComponent } from './cliente.component';
import { CLIENTE_REPOSITORY, ClienteRepository } from '../../../../sales/domain/repositories/cliente.repository';
import { of } from 'rxjs';
import { Cliente } from '../../../../sales/domain/models/cliente.model';
import { provideAnimations } from '@angular/platform-browser/animations';

describe('ClienteComponent', () => {
  let component: ClienteComponent;
  let fixture: ComponentFixture<ClienteComponent>;
  let mockRepository: jasmine.SpyObj<ClienteRepository>;

  beforeEach(async () => {
    mockRepository = jasmine.createSpyObj<ClienteRepository>('ClienteRepository', [
      'listarClientes',
      'obtenerPorId',
      'obtenerPorUsuarioId',
      'registrarCliente',
      'editarCliente',
      'actualizarPerfil',
      'obtenerPerfil',
      'eliminarCliente'
    ]);

    mockRepository.listarClientes.and.returnValue(of([
      new Cliente(1, 101, 'juan', 'juan@gmail.com', '1234567', 'Direccion 1'),
      new Cliente(2, 102, 'maria', 'maria@gmail.com', '7654321', 'Direccion 2')
    ]));

    await TestBed.configureTestingModule({
      imports: [ClienteComponent],
      providers: [
        { provide: CLIENTE_REPOSITORY, useValue: mockRepository },
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clients on init', () => {
    expect(mockRepository.listarClientes).toHaveBeenCalled();
    expect(component.clientes().length).toBe(2);
    expect(component.clientes()[0].usuario).toBe('juan');
  });
});
