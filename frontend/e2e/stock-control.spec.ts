import { test, expect } from '@playwright/test';

test.describe('Flujo E2E: Control de Stock y Catalogo de Productos', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('token', 'fake-jwt-token');
      localStorage.setItem('rol', 'Administrador');
      localStorage.setItem('nombre', 'Administrador Test');
    });
  });

  test('debe cargar la pagina de catalogo de productos en admin', async ({ page }) => {
    await page.goto('/admin/catalog/productos');

    await expect(page).toHaveURL(/\/admin\/catalog\/productos/);
  });

  test('debe cargar la pagina de categorias de productos en admin', async ({ page }) => {
    await page.goto('/admin/catalog/categorias');

    await expect(page).toHaveURL(/\/admin\/catalog\/categorias/);
  });

  test('debe permitir ver el modulo de movimientos de stock en admin', async ({ page }) => {
    await page.goto('/admin/catalog/movimientos/1');

    await expect(page).toHaveURL(/\/admin\/catalog\/movimientos\/1/);
  });
});
