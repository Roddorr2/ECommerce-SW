import { test, expect } from '@playwright/test';

test.describe('Flujo E2E: Control de Stock y Catalogo de Productos', () => {
  test('debe cargar la pagina de catalogo de productos', async ({ page }) => {
    await page.goto('/catalog/productos');

    await expect(page).toHaveTitle(/Productos/i);
  });

  test('debe cargar la pagina de categorias de productos', async ({ page }) => {
    await page.goto('/catalog/categorias');

    await expect(page).toHaveTitle(/Categorías/i);
  });

  test('debe permitir ver el modulo de movimientos de stock', async ({ page }) => {
    await page.goto('/catalog/movimientos/1');

    await expect(page).toHaveTitle(/Movimientos de Stock/i);
  });
});
