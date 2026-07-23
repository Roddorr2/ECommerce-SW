import { MenuItem } from 'primeng/api';

export const ADMIN_MENU_ITEMS: MenuItem[] = [
  {
    label: 'Mi Perfil',
    icon: 'pi pi-user',
    routerLink: ['/admin/perfil'],
  },
  {
    label: 'Catálogo',
    icon: 'pi pi-tags',
    items: [
      { label: 'Productos', icon: 'pi pi-box', routerLink: ['/admin/catalog/productos'] },
      { label: 'Categorías', icon: 'pi pi-list', routerLink: ['/admin/catalog/categorias'] }
    ]
  },
  {
    label: 'Compras',
    icon: 'pi pi-shopping-cart',
    items: [
      { label: 'Compras', icon: 'pi pi-list', routerLink: ['/admin/purchases/compras'] },
      { label: 'Proveedores', icon: 'pi pi-truck', routerLink: ['/admin/purchases/proveedores'] },
      { label: 'Tipos de Proveedor', icon: 'pi pi-tag', routerLink: ['/admin/purchases/tipos-proveedor'] }
    ]
  },
  {
    label: 'Ventas',
    icon: 'pi pi-chart-line',
    items: [
      { label: 'Órdenes', icon: 'pi pi-shopping-cart', routerLink: ['/admin/sales/ordenes'] },
      { label: 'Métodos de Pago', icon: 'pi pi-credit-card', routerLink: ['/admin/sales/metodos-pago'] }
    ]
  },
  {
    label: 'RRHH',
    icon: 'pi pi-users',
    items: [
      { label: 'Empleados', icon: 'pi pi-id-card', routerLink: ['/admin/empleados'] },
      { label: 'Áreas', icon: 'pi pi-building', routerLink: ['/admin/areas'] },
      { label: 'Cargos', icon: 'pi pi-briefcase', routerLink: ['/admin/cargos'] }
    ]
  },
  {
    label: 'Seguridad',
    icon: 'pi pi-shield',
    items: [
      { label: 'Usuarios', icon: 'pi pi-user', routerLink: ['/admin/usuarios'] },
      { label: 'Roles', icon: 'pi pi-lock', routerLink: ['/admin/roles'] },
      { label: 'Clientes', icon: 'pi pi-users', routerLink: ['/admin/clientes'] }
    ]
  }
];