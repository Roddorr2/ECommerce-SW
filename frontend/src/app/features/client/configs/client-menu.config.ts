import { MenuItem } from 'primeng/api';

export const CLIENT_MENU_ITEMS: MenuItem[] = [
  {
    label: 'Dashboard',
    icon: 'pi pi-home',
    routerLink: ['/cliente/dashboard'],
  },
  {
    label: 'Productos',
    icon: 'pi pi-tags',
    routerLink: ['/cliente/productos'],
  },
  {
    label: 'Mis Órdenes',
    icon: 'pi pi-shopping-cart',
    routerLink: ['/cliente/mis-ordenes'],
  },
  {
    label: 'Mi CarritoComponent',
    icon: 'pi pi-shopping-bag',
    routerLink: ['/cliente/carrito'],
  },
  {
    label: 'Mi Perfil',
    icon: 'pi pi-user',
    items: [
      { label: 'Datos personales', icon: 'pi pi-id-card', routerLink: ['/cliente/perfil'] },
      {
        label: 'Cambiar contraseña',
        icon: 'pi pi-key',
        routerLink: ['/cliente/cambiar-contrasena'],
      },
      { label: 'Direcciones', icon: 'pi pi-map-marker', routerLink: ['/cliente/direcciones'] },
    ],
  },
];
