import { test, expect } from '@playwright/test';

test.describe('Módulo de Autenticación', () => {
  test('debe mostrar el formulario de Iniciar Sesión correctamente', async ({ page }) => {
    await page.goto('/auth/login');
    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
    await expect(page.locator('#correo')).toBeVisible();
    await expect(page.locator('#contrasena')).toBeVisible();
  });

  test('debe permitir navegar a la recuperación de contraseña', async ({ page }) => {
    await page.goto('/auth/login');
    await page.getByText('¿Olvidaste tu contraseña?').click();
    await expect(page).toHaveURL(/\/auth\/forgot-password/);
  });
});
