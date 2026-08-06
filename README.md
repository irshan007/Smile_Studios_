# Smile Studios - Full-Stack Photography & Cinematography Application

A luxury, high-performance photography and cinematography website built for **Smile Studios** with a **React 18 + Vite** frontend and a **Spring Boot 3 (Java 21)** backend, connected to **PostgreSQL** for metadata persistence and **Cloudinary** for dynamic image hosting, responsive transformations, and optimization.

Reference aesthetic: Clean dark-and-white luxury photography studio (high-contrast deep obsidian `#0a0a0c`, crisp serif display typography, generous whitespace, champagne gold accents `#d4af37`).

---

## 🌟 Key Features

- **Rebranded Full-Service Studio**: Covers weddings, portraits, pre-weddings, corporate events, maternity/baby sessions, and films across India and globally.
- **10 Portfolio Categories**: *Portraits, Pre Weddings, Tamil Weddings, Telugu Weddings, Brahmin Weddings, Christian Weddings, Muslim Weddings, Engagement, Events, Maternity/Baby*.
- **Granular Multi-Placement Control**: Independent admin placement flags and separate display order fields for:
  - **Homepage Hero Slideshow** (`showInHero`, `heroOrder`)
  - **Homepage Selected Works** (`showInSelectedWorks`, `selectedWorksOrder`)
  - **Category Gallery Page** (`isFeatured`, `displayOrder`)
- **Interactive Admin Management Dashboard (`/admin`)**:
  - Upload tab with placement toggles and order inputs.
  - Image management tab with inline editing (`PATCH /api/admin/images/{id}`) and deletion (`DELETE /api/admin/images/{id}`).
- **Responsive Cloudinary Transformations**: Automatic `f_auto,q_auto,w_*` image URL generation per device breakpoint.
- **Lightbox Viewer**: High-resolution image lightbox powered by `yet-another-react-lightbox`.
- **Lazy-Loaded Video Facades**: Films on `/video` load lightweight poster thumbnails with play overlays; YouTube/Vimeo `iframe`s mount on-demand to preserve page speed.
- **Dynamic SEO & Social Sharing (OpenGraph)**: Dynamic meta descriptions, page titles, and OpenGraph tags (`og:image`, `og:title`, `og:description`) per route using `react-helmet-async`.
- **Spam-Protected Contact Form**: Includes real-time field validation, date picker, and a hidden anti-spam honeypot field (`website`).
- **Automatic Data Seeding**: Spring Boot automatically populates sample images and testimonials across all 10 categories on first run.

---

## 🛠 Backend REST Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/gallery/categories` | List of 10 categories with cover images and photo counts |
| `GET` | `/api/gallery/hero` | Hero slideshow images (`showInHero = true` sorted by `heroOrder`) |
| `GET` | `/api/gallery/selected-works` | Selected works grid images (`showInSelectedWorks = true` sorted by `selectedWorksOrder`) |
| `GET` | `/api/gallery/{category}` | List of images for a category sorted by `displayOrder` |
| `GET` | `/api/testimonials` | List of client testimonials |
| `POST` | `/api/contact` | Handles contact form submissions (`@Valid` validation + honeypot check) |
| `GET` | `/api/admin/images` | Protected by `X-API-KEY`. Returns all images for admin table |
| `POST` | `/api/admin/images` | Protected by `X-API-KEY`. Uploads file to Cloudinary & assigns placements |
| `PATCH` | `/api/admin/images/{id}` | Protected by `X-API-KEY`. Updates placement flags, orders, category, alt text |
| `DELETE` | `/api/admin/images/{id}` | Protected by `X-API-KEY`. Deletes image from database |

---

## 🚀 How to Run Locally

### 1. Prerequisites
- **Node.js**: v18+ (v24 tested)
- **Java**: 17+ (JDK 21 recommended)
- **PostgreSQL** (Optional for local testing; defaults to H2 in-memory DB if Postgres parameters are omitted)

### 2. Start Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```
*The backend server starts on `http://localhost:8080`.*

### 3. Start Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
*The React app starts on `http://localhost:5173`.*

---

## 🔑 Environment Variables

### Backend (`backend/.env` or system env vars)
```ini
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/photography_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

ADMIN_API_KEY=smile_secret_admin_key_2026
```

### Frontend (`frontend/.env`)
```ini
VITE_API_BASE_URL=http://localhost:8080/api
```
