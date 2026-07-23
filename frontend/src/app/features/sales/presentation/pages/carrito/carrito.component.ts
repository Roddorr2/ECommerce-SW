import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from '../../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../../shared/components/footer/footer.component';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { CurrencyPipe } from '@angular/common';
import { MessageService } from 'primeng/api';
import { CARRITO_REPOSITORY } from '../../../domain/repositories/carrito.repository';
import { METODO_PAGO_REPOSITORY } from '../../../domain/repositories/metodo-pago.repository';
import { ORDEN_REPOSITORY } from '../../../domain/repositories/orden.repository';
import { Carrito } from '../../../domain/models/carrito.model';
import { MetodoPago } from '../../../domain/models/metodo-pago.model';

@Component({
  selector: 'app-carrito',
  imports: [
    FormsModule,
    RouterModule,
    ButtonModule,
    SelectModule,
    ToastModule,
    CurrencyPipe,
  ],
  providers: [MessageService],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.scss',
})
export class CarritoComponent implements OnInit {
  private carritoRepository = inject(CARRITO_REPOSITORY);
  private metodoPagoRepository = inject(METODO_PAGO_REPOSITORY);
  private ordenRepository = inject(ORDEN_REPOSITORY);
  private messageService = inject(MessageService);

  carrito = signal<Carrito | null>(null);
  metodosPago = signal<MetodoPago[]>([]);
  metodoPagoSeleccionado = signal<number | null>(null);
  cargando = signal(true);
  procesando = signal(false);

  ngOnInit(): void {
    this.cargarCarrito();
    this.cargarMetodosPago();
  }

  cargarCarrito(): void {
    this.cargando.set(true);
    this.carritoRepository.obtenerCarritoActivo().subscribe({
      next: (data) => {
        this.carrito.set(data);
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.mostrarError('No se pudo cargar el carrito.');
      },
    });
  }

  cargarMetodosPago(): void {
    this.metodoPagoRepository.listarMetodosPago().subscribe({
      next: (data) => this.metodosPago.set(data),
      error: () => this.mostrarError('No se pudieron cargar los métodos de pago.'),
    });
  }

  actualizarItem(itemId: number, cantidad: number): void {
    this.carritoRepository.actualizarItem(itemId, { cantidad }).subscribe({
      next: (data) => this.carrito.set(data),
      error: () => this.mostrarError('No se pudo actualizar la cantidad.'),
    });
  }

  eliminarItem(itemId: number): void {
    this.carritoRepository.eliminarItem(itemId).subscribe({
      next: (data) => this.carrito.set(data),
      error: () => this.mostrarError('No se pudo eliminar el producto.'),
    });
  }

  vaciarCarrito(): void {
    this.carritoRepository.vaciarCarrito().subscribe({
      next: () => this.cargarCarrito(),
      error: () => this.mostrarError('No se pudo vaciar el carrito.'),
    });
  }

  procesarPago(): void {
    if (!this.metodoPagoSeleccionado()) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Método de pago requerido',
        detail: 'Seleccione un método de pago para continuar.',
        life: 3000,
      });
      return;
    }

    this.procesando.set(true);
    this.ordenRepository.crearOrdenDesdeCarrito({
      metodoPagoId: this.metodoPagoSeleccionado()!,
    }).subscribe({
      next: () => {
        this.procesando.set(false);
        this.messageService.add({
          severity: 'success',
          summary: '¡Orden creada!',
          detail: 'Tu orden fue procesada correctamente.',
          life: 3000,
        });
        this.cargarCarrito();
      },
      error: (err) => {
        this.procesando.set(false);
        this.mostrarError(err.error?.mensaje || 'Ocurrió un error al procesar el pago.');
      },
    });
  }

  private mostrarError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail, life: 4000 });
  }
}
