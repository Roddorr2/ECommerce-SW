import { Component, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AUTH_REPOSITORY } from '../../../features/auth/domain/repositories/auth.repository';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterModule, ButtonModule],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {
  private router = inject(Router);
  private authRepository = inject(AUTH_REPOSITORY);

  public currentYear = signal<number>(new Date().getFullYear());

  public navegarA(ruta: string): void {
    this.router.navigate([ruta]);
  }

  public navegarMiCuenta(): void {
    if (!this.authRepository.estaAutenticado()) {
      this.router.navigate(['/auth/login']);
      return;
    }

    const rol = (this.authRepository.obtenerRol() || localStorage.getItem('rol') || '').toUpperCase();
    if (rol === 'CLIENTE') {
      this.router.navigate(['/cliente/perfil']);
    } else if (rol === 'ADMINISTRADOR' || rol === 'ADMIN' || rol === 'EMPLEADO') {
      this.router.navigate(['/admin/perfil']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  public abrirRedSocial(url: string): void {
    window.open(url, '_blank');
  }
}
