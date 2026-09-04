import { test, expect } from '@playwright/test';

test('homepage loads successfully', async ({ page }) => {
  await page.goto('http://localhost:5173/', {
    waitUntil: 'domcontentloaded',
    timeout: 30000
  });

  await expect(page).toHaveTitle(/Smile Studios/i);
});