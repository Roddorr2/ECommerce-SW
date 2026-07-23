import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';

import { routes } from './app.routes';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideAnimations } from '@angular/platform-browser/animations';
import { CATEGORIA_REPOSITORY } from './features/catalog/domain/repositories/categoria.repository';
import { CategoriaHttpRepository } from './features/catalog/infrastructure/repositories/categoria-http.repository';
import { PRODUCTO_REPOSITORY } from './features/catalog/domain/repositories/producto.repository';
import { ProductoHttpRepository } from './features/catalog/infrastructure/repositories/producto-http.repository';
import { MOVIMIENTO_STOCK_REPOSITORY } from './features/catalog/domain/repositories/movimiento-stock.repository';
import { MovimientoStockHttpRepository } from './features/catalog/infrastructure/repositories/movimiento-stock-http.repository';

// Sales Repositories
import { CLIENTE_REPOSITORY } from './features/sales/domain/repositories/cliente.repository';
import { ClienteHttpRepository } from './features/sales/infrastructure/repositories/cliente-http.repository';
import { METODO_PAGO_REPOSITORY } from './features/sales/domain/repositories/metodo-pago.repository';
import { MetodoPagoHttpRepository } from './features/sales/infrastructure/repositories/metodo-pago-http.repository';
import { CARRITO_REPOSITORY } from './features/sales/domain/repositories/carrito.repository';
import { CarritoHttpRepository } from './features/sales/infrastructure/repositories/carrito-http.repository';
import { ORDEN_REPOSITORY } from './features/sales/domain/repositories/orden.repository';
import { OrdenHttpRepository } from './features/sales/infrastructure/repositories/orden-http.repository';

// Purchases Repositories
import { TIPO_PROVEEDOR_REPOSITORY } from './features/purchases/domain/repositories/tipo-proveedor.repository';
import { TipoProveedorHttpRepository } from './features/purchases/infrastructure/repositories/tipo-proveedor-http.repository';
import { PROVEEDOR_REPOSITORY } from './features/purchases/domain/repositories/proveedor.repository';
import { ProveedorHttpRepository } from './features/purchases/infrastructure/repositories/proveedor-http.repository';
import { COMPRA_REPOSITORY } from './features/purchases/domain/repositories/compra.repository';
import { CompraHttpRepository } from './features/purchases/infrastructure/repositories/compra-http.repository';

// Auth Repository
import { AUTH_REPOSITORY } from './features/auth/domain/repositories/auth.repository';
import { AuthHttpRepository } from './features/auth/infrastructure/repositories/auth-http.repository';

// Admin Repositories
import { AREA_REPOSITORY } from './features/admin/domain/repositories/area.repository';
import { AreaHttpRepository } from './features/admin/infrastructure/repositories/area-http.repository';
import { CARGO_REPOSITORY } from './features/admin/domain/repositories/cargo.repository';
import { CargoHttpRepository } from './features/admin/infrastructure/repositories/cargo-http.repository';
import { EMPLEADO_REPOSITORY } from './features/admin/domain/repositories/empleado.repository';
import { EmpleadoHttpRepository } from './features/admin/infrastructure/repositories/empleado-http.repository';
import { ROL_REPOSITORY } from './features/admin/domain/repositories/rol.repository';
import { RolHttpRepository } from './features/admin/infrastructure/repositories/rol-http.repository';
import { USUARIO_REPOSITORY } from './features/admin/domain/repositories/usuario.repository';
import { UsuarioHttpRepository } from './features/admin/infrastructure/repositories/usuario-http.repository';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([
      authInterceptor
    ])),

    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          prefix: 'p',
          darkModeSelector: false,
          cssLayer: false
        }
      }
    }),
    provideAnimations(),
    { provide: CATEGORIA_REPOSITORY, useClass: CategoriaHttpRepository },
    { provide: PRODUCTO_REPOSITORY, useClass: ProductoHttpRepository },
    { provide: MOVIMIENTO_STOCK_REPOSITORY, useClass: MovimientoStockHttpRepository },
    
    // Sales
    { provide: CLIENTE_REPOSITORY, useClass: ClienteHttpRepository },
    { provide: METODO_PAGO_REPOSITORY, useClass: MetodoPagoHttpRepository },
    { provide: CARRITO_REPOSITORY, useClass: CarritoHttpRepository },
    { provide: ORDEN_REPOSITORY, useClass: OrdenHttpRepository },

    // Purchases
    { provide: TIPO_PROVEEDOR_REPOSITORY, useClass: TipoProveedorHttpRepository },
    { provide: PROVEEDOR_REPOSITORY, useClass: ProveedorHttpRepository },
    { provide: COMPRA_REPOSITORY, useClass: CompraHttpRepository },

    // Auth
    { provide: AUTH_REPOSITORY, useClass: AuthHttpRepository },

    // Admin
    { provide: AREA_REPOSITORY, useClass: AreaHttpRepository },
    { provide: CARGO_REPOSITORY, useClass: CargoHttpRepository },
    { provide: EMPLEADO_REPOSITORY, useClass: EmpleadoHttpRepository },
    { provide: ROL_REPOSITORY, useClass: RolHttpRepository },
    { provide: USUARIO_REPOSITORY, useClass: UsuarioHttpRepository }
  ]
};



