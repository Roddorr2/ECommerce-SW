import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { PRODUCTO_REPOSITORY } from '../../../catalog/domain/repositories/producto.repository';
import { AUTH_REPOSITORY } from '../../../auth/domain/repositories/auth.repository';
import { Producto } from '../../../catalog/domain/models/producto.model';
import { SliderComponent } from '../../../../shared/components/slider/slider.component';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [DecimalPipe, HeaderComponent, FooterComponent, SliderComponent, ToastModule],
  providers: [MessageService],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  private productoRepository = inject(PRODUCTO_REPOSITORY);
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private messageService = inject(MessageService);

  public productos = signal<Producto[]>([]);
  public cargando = signal<boolean>(true);

  ngOnInit(): void {
    this.cargarProductos();
  }

  public cargarProductos(): void {
    this.cargando.set(true);
    this.productoRepository.listarProductos().subscribe({
      next: (data: Producto[]) => {
        this.productos.set(data);
        this.cargando.set(false);
      },
      error: (err: Error) => {
        this.cargando.set(false);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron cargar los productos. Intente nuevamente.',
          life: 5000,
        });
      },
    });
  }

  public irAlLogin(): void {
    if (this.authRepository.estaAutenticado()) {
      const rol = this.authRepository.obtenerRol()?.toUpperCase();
      if (rol === 'CLIENTE') {
        this.router.navigate(['/cliente/productos']);
      } else if (rol === 'ADMINISTRADOR' || rol === 'EMPLEADO') {
        this.router.navigate(['/admin/catalog/productos']);
      } else {
        this.router.navigate(['/auth/login']);
      }
    } else {
      this.router.navigate(['/auth/login']);
    }
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

  public obtenerUrlImagen(nombreImagen: string): string {
    return this.productoRepository.obtenerUrlImagen(nombreImagen);
  }

  public mostrarErrorCarga(): void {
    this.messageService.add({
      severity: 'warn',
      summary: 'Conexión lenta',
      detail: 'Los productos están tardando en cargar. Espere un momento...',
      life: 4000,
    });
  }
}
