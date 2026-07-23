import { PanelMenuModule } from 'primeng/panelmenu';
import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../shared/components/footer/footer.component';
import { LayoutService } from '../../../../shared/services/layout.service';
import { AUTH_REPOSITORY } from '../../../auth/domain/repositories/auth.repository';
import { MessageService } from 'primeng/api';
import { CLIENT_MENU_ITEMS } from '../../configs/client-menu.config';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-dashboard-client',
  standalone: true,
  imports: [
    RouterOutlet,
    PanelMenuModule,
    HeaderComponent,
    FooterComponent,
    ToastModule,
    ButtonModule,
  ],
  providers: [MessageService],
  templateUrl: './dashboard-client.component.html',
  styleUrl: './dashboard-client.component.scss',
})
export class DashboardClientComponent {
  private layoutService = inject(LayoutService);
  private authRepository = inject(AUTH_REPOSITORY);
  private messageService = inject(MessageService);
  private router = inject(Router);

  public sidebarVisible = this.layoutService.sidebarVisible;
  public isMobile = this.layoutService.isMobile;
  public isTablet = this.layoutService.isTablet;
  public mostrarOverlay = this.layoutService.mostrarOverlay;

  public menuItems = CLIENT_MENU_ITEMS;

  public nombreUsuario = signal<string | null>(null);
  public bienvenida = computed(() => {
    const nombre = this.nombreUsuario();
    if (nombre) {
      return `Bienvenido de vuelta, ${nombre}`;
    }
    return 'Bienvenido a Technology Fix';
  });

  constructor() {
    this.cargarDatosUsuario();
  }

  private cargarDatosUsuario(): void {
    this.nombreUsuario.set(localStorage.getItem('nombre'));
  }

  public toggleSidebar(): void {
    this.layoutService.toggleSidebar();
  }

  public closeSidebar(): void {
    this.layoutService.closeSidebar();
  }

  public logout(): void {
    this.authRepository.cerrarSesion();
    this.messageService.add({
      severity: 'success',
      summary: 'Sesión cerrada',
      detail: 'Has cerrado sesión correctamente.',
      life: 2000,
    });
    setTimeout(() => {
      this.router.navigate(['/auth/login']);
    }, 2000);
  }
}
