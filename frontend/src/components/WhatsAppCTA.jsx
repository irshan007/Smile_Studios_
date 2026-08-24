import React from 'react';
import { MessageCircle } from 'lucide-react';
import './WhatsAppCTA.css';

const whatsappNumber = '+917200039833';

export const WhatsAppCTA = () => {
  return (
    <a
      className="whatsapp-float"
      href={`https://wa.me/${whatsappNumber.replace(/[^0-9]/g, '')}`}
      target="_blank"
      rel="noopener noreferrer"
      aria-label="Contact via WhatsApp"
    >
      <MessageCircle aria-hidden="true" size={28} />
      <span className="sr-only">WhatsApp</span>
    </a>
  );
};
