import React, { useState } from 'react';
import { Mail, Phone, MapPin, CheckCircle, Send, MessageCircle } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { fetchApi } from '../utils/api';
import './Contact.css';

const InstagramIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
  </svg>
);

const FacebookIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
  </svg>
);

const YoutubeIcon = ({ size = 22, color = 'currentColor' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M2.5 17a24.12 24.12 0 0 1 0-10 2 2 0 0 1 1.4-1.4 49.56 49.56 0 0 1 16.2 0A2 2 0 0 1 21.5 7a24.12 24.12 0 0 1 0 10 2 2 0 0 1-1.4 1.4 49.55 49.55 0 0 1-16.2 0A2 2 0 0 1 2.5 17"></path>
    <path d="m10 15 5-3-5-3z"></path>
  </svg>
);

export function Contact() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    eventDate: '',
    message: '',
    website: '', // Honeypot field
  });

  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [submittedSuccess, setSubmittedSuccess] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.name.trim()) newErrors.name = 'Full name is required';
    if (!formData.email.trim()) {
      newErrors.email = 'Email address is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }
    if (!formData.phone.trim()) newErrors.phone = 'Phone number is required';
    if (!formData.eventDate) newErrors.eventDate = 'Event date is required';
    if (!formData.message.trim()) newErrors.message = 'Please provide details about your event';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    setErrorMessage('');

    try {
      await fetchApi('/contact', {
        method: 'POST',
        body: JSON.stringify(formData),
      });

      setSubmittedSuccess(true);
      setFormData({
        name: '',
        email: '',
        phone: '',
        eventDate: '',
        message: '',
        website: '',
      });
    } catch (err) {
      setErrorMessage(err.message || 'Failed to submit form. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <SeoHead
        title="Contact Studio"
        description="Book a consultation for your upcoming wedding or editorial portrait session."
      />

      <div className="page-header">
        <div className="container">
          <span className="subtitle">RESERVE YOUR DATES</span>
          <h1>Get In Touch</h1>
        </div>
      </div>

      <section className="section-padding container">
        <div className="contact-grid">
          {/* Direct Details Column */}
          <div>
            <span className="section-label">
              LET'S TALK
            </span>
            <h2 style={{ marginTop: '0.5rem', marginBottom: '1.5rem' }}>
              We Would Love To Be Part Of Your Story
            </h2>
            <p style={{ marginBottom: '2.5rem' }}>
              Fill out the form with your event details, location, and dates. We typically respond within 24 hours to schedule an initial consultation call.
            </p>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '1.75rem' }}>
              <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px', flexShrink: 0 }}>
                  <Phone size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Phone & WhatsApp</h4>
                  <p style={{ fontSize: '0.9rem' }}>+917200039833</p>
                  <p style={{ fontSize: '0.8rem', color: 'var(--text-dim)' }}>WhatsApp: Smile Studios</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px' }}>
                  <Mail size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Email Inquiries</h4>
                  <p style={{ fontSize: '0.9rem' }}>hello@photography.com</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px' }}>
                  <InstagramIcon size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Instagram</h4>
                  <p style={{ fontSize: '0.9rem' }}>@smilestudios_official</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px' }}>
                  <MessageCircle size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>WhatsApp</h4>
                  <p style={{ fontSize: '0.9rem' }}>Smile Studios</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px' }}>
                  <FacebookIcon size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Facebook</h4>
                  <p style={{ fontSize: '0.9rem' }}>Smilestudio_official</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px' }}>
                  <YoutubeIcon size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>YouTube</h4>
                  <p style={{ fontSize: '0.9rem' }}>smilestudio222</p>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
                <div className="social-icon-btn" style={{ width: '50px', height: '50px', flexShrink: 0 }}>
                  <MapPin size={22} color="var(--accent-gold)" />
                </div>
                <div>
                  <h4 style={{ color: 'var(--text-main)', fontSize: '1.05rem' }}>Address</h4>
                  <p style={{ fontSize: '0.9rem', lineHeight: 1.6 }}>224, Golden Street, 7th Cross St,<br />Thirumullaivoyal, Chennai,<br />Tamil Nadu 600062</p>
                </div>
              </div>
            </div>
          </div>

          {/* Contact Form Column */}
          <div className="contact-card">
            {submittedSuccess ? (
              <div style={{ textAlign: 'center', padding: '3rem 1rem' }}>
                <CheckCircle size={56} color="var(--accent-gold)" style={{ marginBottom: '1rem' }} />
                <h3 style={{ fontSize: '1.8rem', color: 'var(--text-main)', marginBottom: '0.75rem' }}>
                  Inquiry Sent Successfully!
                </h3>
                <p>Thank you for reaching out. We will review your event details and get back to you within 24 hours.</p>
                <button
                  className="btn-outline"
                  style={{ marginTop: '2rem' }}
                  onClick={() => setSubmittedSuccess(false)}
                >
                  Send Another Message
                </button>
              </div>
            ) : (
              <form className="contact-form" onSubmit={handleSubmit}>
                <h3 style={{ fontSize: '1.6rem', color: 'var(--text-main)', marginBottom: '0.5rem' }}>
                  Check Date Availability
                </h3>

                {errorMessage && (
                  <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #ef4444', color: '#ef4444', padding: '0.9rem', borderRadius: '4px', fontSize: '0.85rem' }}>
                    {errorMessage}
                  </div>
                )}

                {/* Honeypot Input Field (hidden from genuine users) */}
                <div className="honeypot-field">
                  <input
                    type="text"
                    name="website"
                    value={formData.website}
                    onChange={handleChange}
                    tabIndex="-1"
                    autoComplete="off"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="name">Full Name *</label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    className="form-input"
                    placeholder="e.g. Ananya Sharma"
                    value={formData.name}
                    onChange={handleChange}
                  />
                  {errors.name && <span className="error-text">{errors.name}</span>}
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <div className="form-group">
                    <label htmlFor="email">Email Address *</label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      className="form-input"
                      placeholder="e.g. ananya@example.com"
                      value={formData.email}
                      onChange={handleChange}
                    />
                    {errors.email && <span className="error-text">{errors.email}</span>}
                  </div>

                  <div className="form-group">
                    <label htmlFor="phone">Phone / WhatsApp *</label>
                    <input
                      type="tel"
                      id="phone"
                      name="phone"
                      className="form-input"
                      placeholder="+917200039833"
                      value={formData.phone}
                      onChange={handleChange}
                    />
                    {errors.phone && <span className="error-text">{errors.phone}</span>}
                  </div>
                </div>

                <div className="form-group">
                  <label htmlFor="eventDate">Event / Wedding Date *</label>
                  <input
                    type="date"
                    id="eventDate"
                    name="eventDate"
                    className="form-input"
                    value={formData.eventDate}
                    onChange={handleChange}
                  />
                  {errors.eventDate && <span className="error-text">{errors.eventDate}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="message">Event Details & Requirements *</label>
                  <textarea
                    id="message"
                    name="message"
                    rows="5"
                    className="form-input"
                    placeholder="Tell us about your wedding events, venue location, estimated guests, and vision..."
                    value={formData.message}
                    onChange={handleChange}
                  />
                  {errors.message && <span className="error-text">{errors.message}</span>}
                </div>

                <button type="submit" className="btn-primary" disabled={submitting} style={{ width: '100%', justifyContent: 'center' }}>
                  {submitting ? 'Sending Request...' : 'Send Inquiry'} <Send size={18} />
                </button>
              </form>
            )}
          </div>
        </div>
      </section>
    </>
  );
}
