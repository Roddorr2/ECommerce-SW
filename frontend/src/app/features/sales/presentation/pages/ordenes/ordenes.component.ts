import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ORDEN_REPOSITORY } from '../../../domain/repositories/orden.repository';
import { EstadoOrden } from '../../../domain/models/estado-orden.enum';
import { Orden, OrdenResumen } from '../../../domain/models/orden.model';

@Component({
  selector: 'app-ordenes',
  imports: [
    TableModule,
    ButtonModule,
    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    SelectModule,
    DatePipe,
    CurrencyPipe,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './ordenes.component.html',
  styleUrl: './ordenes.component.scss',
})
export class OrdenesComponent implements OnInit {
  private ordenRepository = inject(ORDEN_REPOSITORY);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  protected readonly EstadoOrden = EstadoOrden;

  ordenes = signal<OrdenResumen[]>([]);
  ordenSeleccionada = signal<Orden | null>(null);
  cargando = signal(true);
  mostrarDetalle = signal(false);

  estadosOpciones = [
    { label: 'Pendiente', value: EstadoOrden.PENDIENTE },
    { label: 'Pagado', value: EstadoOrden.PAGADO },
    { label: 'Enviado', value: EstadoOrden.ENVIADO },
    { label: 'Entregado', value: EstadoOrden.ENTREGADO },
    { label: 'Cancelado', value: EstadoOrden.CANCELADO },
  ];

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando.set(true);
    this.ordenRepository.listarOrdenes().subscribe({
      next: (data) => {
        this.ordenes.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudieron cargar las órdenes.');
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

  cambiarEstado(ordenId: number, estado: EstadoOrden): void {
    this.ordenRepository.cambiarEstado(ordenId, { estado }).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Estado actualizado',
          detail: 'El estado de la orden fue actualizado.',
          life: 3000,
        });
        this.cargarOrdenes();
      },
      error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo actualizar el estado.'),
    });
  }

  confirmarCancelar(ordenId: number): void {
    this.confirmationService.confirm({
      message: '¿Está seguro de cancelar esta orden? Se devolverá el stock.',
      header: 'Confirmar cancelación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, cancelar',
      rejectLabel: 'No',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.ordenRepository.cancelarOrden(ordenId).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Orden cancelada',
              detail: 'Stock devuelto correctamente.',
              life: 3000,
            });
            this.cargarOrdenes();
          },
          error: (err) => this.mostrarError(err.error?.mensaje || 'No se pudo cancelar la orden.'),
        });
      },
    });
  }

  getBadgeClass(estado: EstadoOrden): string {
    switch (estado) {
      case EstadoOrden.PENDIENTE:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-amber-50 text-amber-700 border border-amber-200/50';
      case EstadoOrden.PAGADO:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-sky-50 text-sky-700 border border-sky-200/50';
      case EstadoOrden.ENVIADO:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-indigo-50 text-indigo-700 border border-indigo-200/50';
      case EstadoOrden.ENTREGADO:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200/50';
      case EstadoOrden.CANCELADO:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-rose-50 text-rose-700 border border-rose-200/50';
      default:
        return 'px-2.5 py-1 text-xs font-bold rounded-full bg-slate-50 text-slate-700 border border-slate-200/50';
    }
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
