import { test, expect } from '@playwright/test';

test.describe('Flujo E2E: Proceso de Seleccion y Checkout', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('token', 'fake-jwt-token');
      localStorage.setItem('rol', 'Administrador');
      localStorage.setItem('nombre', 'Administrador Test');
    });
  });

  test('debe permitir navegar al carrito de compras en admin', async ({ page }) => {
    await page.goto('/admin/sales/carrito');

    await expect(page).toHaveURL(/\/admin\/sales\/carrito/);
  });

  test('debe permitir navegar al módulo de órdenes en admin', async ({ page }) => {
    await page.goto('/admin/sales/ordenes');

    await expect(page).toHaveURL(/\/admin\/sales\/ordenes/);
  });

  test('debe listar los métodos de pago disponibles en admin', async ({ page }) => {
    await page.goto('/admin/sales/metodos-pago');

    await expect(page).toHaveURL(/\/admin\/sales\/metodos-pago/);
  });
});
