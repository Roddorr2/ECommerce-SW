import { test, expect } from '@playwright/test';

test.describe('Flujo E2E: Proceso de Seleccion y Checkout', () => {
  test('debe permitir navegar al carrito de compras', async ({ page }) => {
    await page.goto('/sales/carrito');

    await expect(page).toHaveTitle(/Carrito/i);
  });

  test('debe permitir navegar al módulo de órdenes', async ({ page }) => {
    await page.goto('/sales/ordenes');

    await expect(page).toHaveTitle(/Órdenes/i);
  });

  test('debe listar los métodos de pago disponibles', async ({ page }) => {
    await page.goto('/sales/metodos-pago');

    await expect(page).toHaveTitle(/Métodos de Pago/i);
  });
});
