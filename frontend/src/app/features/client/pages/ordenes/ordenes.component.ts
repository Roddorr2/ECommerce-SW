import { Component, inject, OnInit, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { EstadoOrden } from '../../../sales/domain/models/estado-orden.enum';
import { ORDEN_REPOSITORY } from '../../../sales/domain/repositories/orden.repository';
import { Orden, OrdenResumen } from '../../../sales/domain/models/orden.model';

@Component({
  selector: 'app-ordenes',
  imports: [HeaderComponent, FooterComponent, ButtonModule, ToastModule, DialogModule, DatePipe, CurrencyPipe],
  providers: [MessageService],
  templateUrl: './ordenes.component.html',
  styleUrl: './ordenes.component.scss',
})
export class OrdenesClienteComponent implements OnInit {
  private ordenRepository = inject(ORDEN_REPOSITORY);
  private messageService = inject(MessageService);

  protected readonly EstadoOrden = EstadoOrden;

  ordenes = signal<OrdenResumen[]>([]);
  ordenSeleccionada = signal<Orden | null>(null);
  cargando = signal(true);
  mostrarDetalle = signal(false);

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando.set(true);
    this.ordenRepository.obtenerMisOrdenes().subscribe({
      next: (data) => {
        this.ordenes.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar tus órdenes.');
      },
    });
  }

  verDetalle(id: number): void {
    this.ordenRepository.obtenerPorId(id).subscribe({
      next: (data) => {
        this.ordenSeleccionada.set(data);
        this.mostrarDetalle.set(true);
      },
      error: () => this.mostrarError('No se pudo cargar el detalle.'),
    });
  }

  cancelarOrden(ordenId: number): void {
    this.ordenRepository.cancelarOrden(ordenId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Orden cancelada',
          detail: 'Tu orden fue cancelada correctamente.',
          life: 3000,
        });
        this.mostrarDetalle.set(false);
        this.cargarOrdenes();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo cancelar la orden.'),
    });
  }

  getBadgeClass(estado: EstadoOrden): string {
    switch (estado) {
      case EstadoOrden.PENDIENTE:
        return 'badge bg-warning';
      case EstadoOrden.PAGADO:
        return 'badge bg-info';
      case EstadoOrden.ENVIADO:
        return 'badge bg-primary';
      case EstadoOrden.ENTREGADO:
        return 'badge bg-success';
      case EstadoOrden.CANCELADO:
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
