import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ADMIN_MENU_ITEMS } from '../../../configs/admin-menu.config';
import { HeaderComponent } from '../../../../../shared/components/header/header.component';
import { FooterComponent } from '../../../../../shared/components/footer/footer.component';
import { LayoutService } from '../../../../../shared/services/layout.service';
import { AUTH_REPOSITORY } from '../../../../auth/domain/repositories/auth.repository';

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [RouterOutlet, MenubarModule, PanelMenuModule, ButtonModule, HeaderComponent, FooterComponent],
  providers: [MessageService],
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.scss'],
})
export class DashboardAdminComponent {
  private readonly layoutService = inject(LayoutService);
  private readonly authRepository = inject(AUTH_REPOSITORY);
  private readonly messageService = inject(MessageService);
  private readonly router = inject(Router);

  public sidebarVisible = this.layoutService.sidebarVisible;
  public isMobile = this.layoutService.isMobile;
  public isTablet = this.layoutService.isTablet;
  public mostrarOverlay = this.layoutService.mostrarOverlay;

  public menuItems = ADMIN_MENU_ITEMS;

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
