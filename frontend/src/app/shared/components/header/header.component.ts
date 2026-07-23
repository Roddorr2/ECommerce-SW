import {Component, computed, DestroyRef, inject, OnInit, signal} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AUTH_REPOSITORY } from '../../../features/auth/domain/repositories/auth.repository';

@Component({
  selector: 'app-header',
  imports: [RouterModule, ButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  private authRepository = inject(AUTH_REPOSITORY);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  public isAuthenticated = signal<boolean>(false);
  public nombreUsuario = signal<string | null>(null);
  public rol = signal<string | null>(null);

  public esCliente = computed(() => {
    const userRol = this.rol();
    if (!userRol) return false;
    return userRol.toLowerCase() === 'cliente';
  });

  public esAdmin = computed(() => {
    const userRol = this.rol();
    if (!userRol) return false;
    return userRol.toLowerCase() === 'administrador' || userRol.toLowerCase() === 'admin';
  });

  ngOnInit(): void {
    this.verificarAutenticacion();
  }

  public verificarAutenticacion(): void {
    const token = this.authRepository.obtenerToken();
    this.isAuthenticated.set(!!token);

    if (this.isAuthenticated()) {
      this.nombreUsuario.set(localStorage.getItem('nombre'));
      this.rol.set(this.authRepository.obtenerRol() || localStorage.getItem('rol'));
    } else {
      this.nombreUsuario.set(null);
      this.rol.set(null);
    }
  }

  public navegarHome(): void {
    this.router.navigate(['/home']);
  }

  public navegarLogin(): void {
    this.router.navigate(['/auth/login']);
  }

  public navegarProductos(): void {
    this.router.navigate(['/cliente/productos']);
  }

  public navegarCarrito(): void {
    this.router.navigate(['/cliente/carrito']);
  }

  public navegarOrdenes(): void {
    this.router.navigate(['/cliente/mis-ordenes']);
  }

  public cerrarSesion(): void {
    this.authRepository.cerrarSesion();
    this.isAuthenticated.set(false);
    this.nombreUsuario.set(null);
    this.rol.set(null);
    this.router.navigate(['/auth/login']);
  }
}
