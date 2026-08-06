import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';

import { Navbar } from './components/Navbar';
import { Footer } from './components/Footer';

import { Home } from './pages/Home';
import { About } from './pages/About';
import { PortfolioCategories } from './pages/PortfolioCategories';
import { CategoryGallery } from './pages/CategoryGallery';
import { VideoFilms } from './pages/VideoFilms';
import { Contact } from './pages/Contact';
import { AdminUpload } from './pages/AdminUpload';

export function App() {
  return (
    <HelmetProvider>
      <Router>
        <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <Navbar />
          <main style={{ flex: 1 }}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/about" element={<About />} />
              <Route path="/portfolio" element={<PortfolioCategories />} />
              <Route path="/portfolio/:category" element={<CategoryGallery />} />
              <Route path="/video" element={<VideoFilms />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="/admin" element={<AdminUpload />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </HelmetProvider>
  );
}

export default App;
