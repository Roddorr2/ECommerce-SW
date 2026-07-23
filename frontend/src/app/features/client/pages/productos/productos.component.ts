import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { PRODUCTO_REPOSITORY } from '../../../catalog/domain/repositories/producto.repository';
import { CARRITO_REPOSITORY } from '../../../sales/domain/repositories/carrito.repository';
import { Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { FormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { Producto } from '../../../catalog/domain/models/producto.model';
import { DecimalPipe, SlicePipe } from '@angular/common';

@Component({
  selector: 'app-productos',
  imports: [
    FormsModule,
    RouterModule,
    ToastModule,
    ButtonModule,
    InputNumberModule,
    DecimalPipe,
    SlicePipe,
  ],
  providers: [MessageService],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.scss',
})
export class ProductosClienteComponent implements OnInit {
  private productoRepository = inject(PRODUCTO_REPOSITORY);
  private carritoRepository = inject(CARRITO_REPOSITORY);
  private router = inject(Router);
  private messageService = inject(MessageService);

  public productos = signal<Producto[]>([]);
  public cargando = signal<boolean>(true);
  public cantidades = signal<Record<number, number>>({});

  public tieneProductos = computed(() => this.productos().length > 0);
  public productosDisponibles = computed(() =>
    this.productos().filter((p) => p.activo && p.stock > 0),
  );

  ngOnInit(): void {
    this.cargarProductos();
  }

  public cargarProductos(): void {
    this.cargando.set(true);
    this.productoRepository.listarProductos().subscribe({
      next: (data: Producto[]) => {
        this.productos.set(data || []);

        const nuevasCantidades: Record<number, number> = {};
        (data || []).forEach((producto: Producto) => {
          if (producto.activo && producto.stock > 0) {
            nuevasCantidades[producto.id] = 1;
          }
        });
        this.cantidades.set(nuevasCantidades);

        this.cargando.set(false);
      },
      error: (err: Error) => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los productos.',
          life: 4000,
        });
      },
    });
  }

  public obtenerCantidad(productoId: number): number {
    return this.cantidades()[productoId] ?? 1;
  }

  public actualizarCantidad(productoId: number, cantidad: number): void {
    const producto = this.productos().find((p) => p.id === productoId);
    if (!producto) return;

    if (cantidad < 1) {
      cantidad = 1;
    }
    if (cantidad > producto.stock) {
      this.fancyStockWarning(producto.stock);
      return;
    }

    const nuevasCantidades = { ...this.cantidades() };
    nuevasCantidades[productoId] = cantidad;
    this.cantidades.set(nuevasCantidades);
  }

  private fancyStockWarning(stock: number): void {
    this.messageService.add({
      severity: 'warn',
      summary: 'Stock limitado',
      detail: `Solo hay ${stock} unidades disponibles.`,
      life: 3000,
    });
  }

  public agregarAlCarrito(producto: Producto): void {
    if (!producto) return;

    if (!producto.activo) {
      this.messageService.add({
        severity: 'error',
        summary: 'No disponible',
        detail: 'Este producto no está disponible actualmente.',
        life: 3000,
      });
      return;
    }

    if (producto.stock <= 0) {
      this.messageService.add({
        severity: 'error',
        summary: 'Agotado',
        detail: 'Este producto está agotado.',
        life: 3000,
      });
      return;
    }

    const cantidad = this.obtenerCantidad(producto.id);

    if (cantidad > producto.stock) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: `No hay suficiente stock. Disponible: ${producto.stock}`,
        life: 3000,
      });
      return;
    }

    this.carritoRepository
      .agregarItem({
        productoId: producto.id,
        cantidad: cantidad,
      })
      .subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Agregado',
            detail: `${producto.nombre} x${cantidad} agregado al carrito.`,
            life: 2000,
          });
        },
        error: (err: Error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: err.message || 'No se pudo agregar el producto al carrito.',
            life: 4000,
          });
        },
      });
  }

  public verDetalle(id: number): void {
    this.router.navigate(['/cliente/productos', id]);
  }

  public onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src =
      'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24" fill="none" stroke="%23666" stroke-width="1" stroke-linecap="round" stroke-linejoin="round"%3E%3Crect x="3" y="3" width="18" height="18" rx="2" ry="2"%3E%3C/rect%3E%3Ccircle cx="8.5" cy="8.5" r="1.5"%3E%3C/circle%3E%3Cpolyline points="21 15 16 10 5 21"%3E%3C/polyline%3E%3C/svg%3E';
    img.style.objectFit = 'contain';
    img.style.padding = '2rem';
    img.style.backgroundColor = '#f5f5f5';
    img.onerror = null;
  }

  public irAlCarrito(): void {
    this.router.navigate(['/cliente/carrito']);
  }

  public obtenerUrlImagen(nombreImagen: string): string {
    return this.productoRepository.obtenerUrlImagen(nombreImagen);
  }
}
