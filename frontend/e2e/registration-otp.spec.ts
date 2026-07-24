import { test, expect } from '@playwright/test';

test.describe('Flujo E2E: Registro de Usuario y Verificación 2FA/OTP', () => {
  test('debe mostrar la pantalla de registro y validar los campos requeridos', async ({ page }) => {
    await page.goto('/auth/register');

    await expect(page.getByRole('heading', { name: 'Crear Cuenta' })).toBeVisible();
    await expect(page.locator('#nombre')).toBeVisible();
    await expect(page.locator('#correo')).toBeVisible();
    await expect(page.locator('#telefono')).toBeVisible();
    await expect(page.locator('#contrasena')).toBeVisible();
    await expect(page.locator('#direccion')).toBeVisible();
  });

  test('debe navegar a la vista de verificación de código OTP', async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('correo_2fa', 'test.otp@ejemplo.com');
    });

    await page.goto('/auth/verificar-codigo');

    await expect(page.getByRole('heading', { name: /Verificación/i })).toBeVisible();
    await expect(page.getByRole('button', { name: /Verificar Código/i })).toBeVisible();
  });

  test('debe permitir ingresar los dígitos del código 2FA', async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('correo_2fa', 'test.otp@ejemplo.com');
    });

    await page.goto('/auth/verificar-codigo');

    const inputs = page.locator('input[maxlength="1"]');
    const count = await inputs.count();
    expect(count).toBe(6);

    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill(String(i + 1));
    }

    await expect(inputs.nth(5)).toHaveValue('6');
  });
});
