import { TipoMovimiento } from '../../../domain/models/tipo-movimiento.enum';
import { MovimientoStock } from '../../../domain/models/movimiento-stock.model';
import { DatePipe } from '@angular/common';
import { Component, inject, input, OnInit, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { MOVIMIENTO_STOCK_REPOSITORY } from '../../../domain/repositories/movimiento-stock.repository';
import { TipoReferencia } from '../../../domain/models/tipo-referencia.enum';

@Component({
  selector: 'app-movimiento-stock',
  imports: [TableModule, ToastModule, ButtonModule, DatePipe, RouterModule],
  providers: [MessageService],
  templateUrl: './movimiento-stock.component.html',
  styleUrl: './movimiento-stock.component.scss',
})
export class MovimientoStockComponent implements OnInit {
  private movimientoRepository = inject(MOVIMIENTO_STOCK_REPOSITORY);
  private messageService = inject(MessageService);

  productoId = input.required<number>();

  protected readonly TipoMovimiento = TipoMovimiento;
  protected readonly TipoReferencia = TipoReferencia;

  movimientos = signal<MovimientoStock[]>([]);
  cargando = signal(true);

  ngOnInit(): void {
    this.movimientoRepository.obtenerMovimientosPorProducto(this.productoId()).subscribe({
      next: (data) => {
        this.movimientos.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los movimientos.',
          life: 4000,
        });
      },
    });
  }
}
