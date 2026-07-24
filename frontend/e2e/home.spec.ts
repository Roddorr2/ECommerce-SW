import { test, expect } from '@playwright/test';

test.describe('Navegación Pública y Página de Inicio', () => {
  test('debe cargar la página principal y verificar el título', async ({ page }) => {
    await page.goto('/home');
    await expect(page).toHaveTitle(/Inicio/i);
  });

  test('debe permitir navegar a la página de Preguntas Frecuentes', async ({ page }) => {
    await page.goto('/faq');
    await expect(page).toHaveTitle(/Preguntas Frecuentes/i);
  });

  test('debe permitir navegar a Métodos de Pago', async ({ page }) => {
    await page.goto('/metodos-pago');
    await expect(page).toHaveTitle(/Métodos de Pago/i);
  });

  test('debe permitir navegar a Soporte Técnico', async ({ page }) => {
    await page.goto('/soporte-tecnico');
    await expect(page).toHaveTitle(/Soporte Técnico/i);
  });
});
