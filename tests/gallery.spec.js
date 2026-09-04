import { test, expect } from '@playwright/test';

test('user can open gallery photos', async ({ page }) => {
  await page.goto('http://localhost:5173/', {
    waitUntil: 'domcontentloaded'
  });

  // Open all galleries
  await page.getByRole('link', { name: 'View All Galleries' }).click();

  // Open Weddings gallery
  await page
    .getByRole('link', { name: 'Weddings Weddings Ceremony,' })
    .click();

  // Open first photo
  await page
    .getByRole('button', { name: 'Open Weddings photo 1' })
    .click();

  // Lightbox should appear
  await expect(page.locator('.yarl__container')).toBeVisible();

  // Navigate to next photo
  await page.locator('.yarl__container').press('ArrowRight');

  // Close lightbox
  await page.getByRole('button', { name: 'Close' }).click();

  await expect(page.locator('.yarl__container')).not.toBeVisible();
});