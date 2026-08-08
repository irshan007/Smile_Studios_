import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';

import { Navbar } from './components/Navbar';
import { Footer } from './components/Footer';
import { WhatsAppCTA } from './components/WhatsAppCTA';

import { Home } from './pages/Home';
import { ServiceCategory } from './pages/ServiceCategory';
import { Albums } from './pages/Albums';
import { Contact } from './pages/Contact';

export function App() {
  return (
    <HelmetProvider>
      <Router>
        <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <Navbar />
          <main style={{ flex: 1 }}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/services/:category" element={<ServiceCategory />} />
              <Route path="/albums" element={<Albums />} />
              <Route path="/contact" element={<Contact />} />
            </Routes>
          </main>
          <Footer />
          <WhatsAppCTA />
        </div>
      </Router>
    </HelmetProvider>
  );
}

export default App;
