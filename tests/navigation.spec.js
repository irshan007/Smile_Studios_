import { test, expect } from '@playwright/test';

test('main navigation works', async ({ page }) => {
  // Home
  await page.goto('http://localhost:5173/');
  await expect(page).toHaveTitle(/Smile Studios/i);

  // About
  await page.getByRole('link', { name: 'About' }).first().click();
  await expect(page).toHaveURL(/\/about/i);

  // Portfolio
  await page.getByRole('link', { name: 'Portfolio', exact: true }).click();
  await expect(page).toHaveURL(/\/portfolio/i);

  // Home
  await page.getByRole('link', { name: 'Home' }).first().click();
  await expect(page).toHaveURL(/\/$/);

  // All Galleries
  await page.getByRole('link', { name: 'All Galleries', exact: true }).click();
  await expect(page).toHaveURL(/\/portfolio/i);
});