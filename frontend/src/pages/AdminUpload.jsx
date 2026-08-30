import React, { useState, useEffect, useCallback } from 'react';
import { Upload, Key, CheckCircle, AlertCircle, Image as ImageIcon, Trash2, Save, Layers, Edit, Plus, X, Lock, LogOut, Eye, EyeOff } from 'lucide-react';
import { SeoHead } from '../components/SeoHead';
import { fetchApi } from '../utils/api';

export function AdminUpload() {
  // Authentication State
  const [jwtToken, setJwtToken] = useState(localStorage.getItem('admin_jwt_token') || '');
  const [userInfo, setUserInfo] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('admin_user_info') || 'null');
    } catch {
      return null;
    }
  });
  const [authenticated, setAuthenticated] = useState(Boolean(jwtToken));

  // Login Form State
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [loggingIn, setLoggingIn] = useState(false);

  // Tab & Notification State
  const [activeTab, setActiveTab] = useState('create'); // 'create' | 'manage'
  const [statusMessage, setStatusMessage] = useState(null);

  // Dynamic Categories from Backend
  const [categories, setCategories] = useState([]);

  // --- Create Form State ---
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('');
  const [description, setDescription] = useState('');
  const [coverFile, setCoverFile] = useState(null);
  const [coverPreviewUrl, setCoverPreviewUrl] = useState(null);
  const [galleryFiles, setGalleryFiles] = useState([]);
  const [galleryPreviews, setGalleryPreviews] = useState([]);
  const [isPublished, setIsPublished] = useState(true);
  const [isFeatured, setIsFeatured] = useState(false);
  const [showInHero, setShowInHero] = useState(false);
  const [showInSelectedWorks, setShowInSelectedWorks] = useState(false);
  const [displayOrder, setDisplayOrder] = useState(0);
  const [creating, setCreating] = useState(false);

  // --- Manage Works State ---
  const [worksList, setWorksList] = useState([]);
  const [loadingWorks, setLoadingWorks] = useState(false);
  const [editingWork, setEditingWork] = useState(null);

  // --- Edit Work Form State ---
  const [editTitle, setEditTitle] = useState('');
  const [editCategory, setEditCategory] = useState('');
  const [editDescription, setEditDescription] = useState('');
  const [editDisplayOrder, setEditDisplayOrder] = useState(0);
  const [editIsPublished, setEditIsPublished] = useState(false);
  const [editIsFeatured, setEditIsFeatured] = useState(false);
  const [editShowInHero, setEditShowInHero] = useState(false);
  const [editShowInSelectedWorks, setEditShowInSelectedWorks] = useState(false);
  const [newCoverFile, setNewCoverFile] = useState(null);
  const [newCoverPreviewUrl, setNewCoverPreviewUrl] = useState(null);
  const [newGalleryFiles, setNewGalleryFiles] = useState([]);
  const [newGalleryPreviews, setNewGalleryPreviews] = useState([]);
  const [existingImages, setExistingImages] = useState([]);
  const [updating, setUpdating] = useState(false);

  // Fetch Categories from Backend GET /api/gallery/categories
  const loadCategories = useCallback(async () => {
    try {
      const res = await fetchApi('/gallery/categories');
      if (res?.data && Array.isArray(res.data)) {
        setCategories(res.data);
        if (res.data.length > 0 && !category) {
          setCategory(res.data[0].name || res.data[0].slug);
        }
      }
    } catch (err) {
      console.error('Failed to load backend categories:', err);
    }
  }, [category]);

  // Load All Works for Admin GET /api/admin/works
  const loadAdminWorks = useCallback(async () => {
    if (!authenticated) return;
    setLoadingWorks(true);
    try {
      const res = await fetchApi('/admin/works');
      if (res?.data) {
        setWorksList(res.data);
      }
    } catch (err) {
      console.error('Failed to load portfolio works:', err);
      if (err.message?.includes('expired') || err.message?.includes('401')) {
        handleLogout('Your session has expired. Please log in again.');
      } else {
        setStatusMessage({ type: 'error', text: err.message || 'Failed to load portfolio works.' });
      }
    } finally {
      setLoadingWorks(false);
    }
  }, [authenticated]);

  useEffect(() => {
    loadCategories();
  }, [loadCategories]);

  useEffect(() => {
    if (authenticated && activeTab === 'manage') {
      loadAdminWorks();
    }
  }, [authenticated, activeTab, loadAdminWorks]);

  // --- Handlers ---

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!loginEmail.trim() || !loginPassword.trim()) {
      setStatusMessage({ type: 'error', text: 'Please enter both email and password.' });
      return;
    }

    setLoggingIn(true);
    setStatusMessage(null);

    try {
      const res = await fetchApi('/admin/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email: loginEmail.trim(), password: loginPassword }),
      });

      if (res.token) {
        localStorage.setItem('admin_jwt_token', res.token);
        localStorage.setItem('admin_user_info', JSON.stringify(res.user));
        setJwtToken(res.token);
        setUserInfo(res.user);
        setAuthenticated(true);
        setStatusMessage({ type: 'success', text: 'Authentication successful! Welcome to Admin Dashboard.' });
      }
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || 'Invalid email or password.' });
    } finally {
      setLoggingIn(false);
    }
  };

  const handleLogout = (message = null) => {
    localStorage.removeItem('admin_jwt_token');
    localStorage.removeItem('admin_user_info');
    setJwtToken('');
    setUserInfo(null);
    setAuthenticated(false);
    setStatusMessage(message ? { type: 'error', text: message } : null);
  };

  // Cover Image Selection
  const handleCoverChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (coverPreviewUrl) URL.revokeObjectURL(coverPreviewUrl);
      setCoverFile(file);
      setCoverPreviewUrl(URL.createObjectURL(file));
    }
  };

  const removeCoverImage = () => {
    if (coverPreviewUrl) URL.revokeObjectURL(coverPreviewUrl);
    setCoverFile(null);
    setCoverPreviewUrl(null);
  };

  // Multiple Gallery Images Selection
  const handleGalleryChange = (e) => {
    const files = Array.from(e.target.files);
    if (files.length > 0) {
      const newFiles = [...galleryFiles, ...files];
      const newPreviews = files.map((f) => URL.createObjectURL(f));
      setGalleryFiles(newFiles);
      setGalleryPreviews((prev) => [...prev, ...newPreviews]);
    }
  };

  const removeGalleryImage = (index) => {
    URL.revokeObjectURL(galleryPreviews[index]);
    setGalleryFiles((prev) => prev.filter((_, i) => i !== index));
    setGalleryPreviews((prev) => prev.filter((_, i) => i !== index));
  };

  // Create Work Form Submission
  const handleCreateWork = async (e) => {
    e.preventDefault();

    if (!title.trim()) {
      setStatusMessage({ type: 'error', text: 'Portfolio work title is required.' });
      return;
    }
    if (!category) {
      setStatusMessage({ type: 'error', text: 'Please select a valid category.' });
      return;
    }
    if (!coverFile) {
      setStatusMessage({ type: 'error', text: 'Please select a cover image for the work.' });
      return;
    }

    setCreating(true);
    setStatusMessage(null);

    const formData = new FormData();
    formData.append('title', title.trim());
    formData.append('category', category);
    formData.append('description', description.trim());
    formData.append('coverImage', coverFile);
    galleryFiles.forEach((file) => formData.append('galleryImages', file));
    formData.append('isPublished', isPublished);
    formData.append('isFeatured', isFeatured);
    formData.append('showInHero', showInHero);
    formData.append('showInSelectedWorks', showInSelectedWorks);
    formData.append('displayOrder', displayOrder);

    try {
      const res = await fetchApi('/admin/works', {
        method: 'POST',
        body: formData,
      });

      setStatusMessage({
        type: 'success',
        text: `Portfolio Work '${res.data?.title || title}' created successfully!`,
      });

      // Reset Create Form
      setTitle('');
      setDescription('');
      removeCoverImage();
      galleryPreviews.forEach((url) => URL.revokeObjectURL(url));
      setGalleryFiles([]);
      setGalleryPreviews([]);
      setDisplayOrder(0);

      // Refresh list if manage tab active
      loadAdminWorks();
    } catch (err) {
      if (err.message?.includes('expired') || err.message?.includes('401')) {
        handleLogout('Your session has expired. Please log in again.');
      } else {
        setStatusMessage({ type: 'error', text: err.message || 'Failed to create portfolio work.' });
      }
    } finally {
      setCreating(false);
    }
  };

  // Open Edit Modal for a Work
  const openEditModal = (work) => {
    setEditingWork(work);
    setEditTitle(work.title || '');
    setEditCategory(work.category || (categories[0]?.name || ''));
    setEditDescription(work.description || '');
    setEditDisplayOrder(work.displayOrder || 0);
    setEditIsPublished(Boolean(work.isPublished));
    setEditIsFeatured(Boolean(work.isFeatured));
    setEditShowInHero(Boolean(work.showInHero));
    setEditShowInSelectedWorks(Boolean(work.showInSelectedWorks));
    setExistingImages(work.images || []);
    setNewCoverFile(null);
    if (newCoverPreviewUrl) URL.revokeObjectURL(newCoverPreviewUrl);
    setNewCoverPreviewUrl(null);
    newGalleryPreviews.forEach((url) => URL.revokeObjectURL(url));
    setNewGalleryFiles([]);
    setNewGalleryPreviews([]);
  };

  const closeEditModal = () => {
    if (newCoverPreviewUrl) URL.revokeObjectURL(newCoverPreviewUrl);
    newGalleryPreviews.forEach((url) => URL.revokeObjectURL(url));
    setEditingWork(null);
  };

  // Delete Individual Existing Gallery Image from Work
  const handleDeleteExistingImage = async (imageId) => {
    if (!editingWork) return;
    if (!window.confirm('Are you sure you want to delete this gallery image? This will remove it from Cloudinary and database.')) {
      return;
    }

    try {
      await fetchApi(`/admin/works/${editingWork.id}/images/${imageId}`, {
        method: 'DELETE',
      });
      setExistingImages((prev) => prev.filter((img) => img.id !== imageId));
      setStatusMessage({ type: 'success', text: 'Gallery image deleted successfully.' });
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || 'Failed to delete gallery image.' });
    }
  };

  // Submit Edit Work Form
  const handleUpdateWork = async (e) => {
    e.preventDefault();
    if (!editingWork) return;

    if (!editTitle.trim()) {
      setStatusMessage({ type: 'error', text: 'Portfolio work title is required.' });
      return;
    }
    if (!editCategory) {
      setStatusMessage({ type: 'error', text: 'Please select a valid category.' });
      return;
    }

    setUpdating(true);
    setStatusMessage(null);

    const formData = new FormData();
    formData.append('title', editTitle.trim());
    formData.append('category', editCategory);
    formData.append('description', editDescription.trim());
    if (newCoverFile) {
      formData.append('coverImage', newCoverFile);
    }
    newGalleryFiles.forEach((file) => formData.append('galleryImages', file));
    formData.append('isPublished', editIsPublished);
    formData.append('isFeatured', editIsFeatured);
    formData.append('showInHero', editShowInHero);
    formData.append('showInSelectedWorks', editShowInSelectedWorks);
    formData.append('displayOrder', editDisplayOrder);

    try {
      const res = await fetchApi(`/admin/works/${editingWork.id}`, {
        method: 'PUT',
        body: formData,
      });

      setStatusMessage({ type: 'success', text: `Portfolio Work '${res.data?.title}' updated successfully!` });
      closeEditModal();
      loadAdminWorks();
    } catch (err) {
      if (err.message?.includes('expired') || err.message?.includes('401')) {
        handleLogout('Your session has expired. Please log in again.');
      } else {
        setStatusMessage({ type: 'error', text: err.message || 'Failed to update portfolio work.' });
      }
    } finally {
      setUpdating(false);
    }
  };

  // Toggle Publish / Unpublish
  const handleTogglePublish = async (work) => {
    const targetStatus = !work.isPublished;
    try {
      const res = await fetchApi(`/admin/works/${work.id}/publish?published=${targetStatus}`, {
        method: 'PATCH',
      });
      setWorksList((prev) =>
        prev.map((w) => (w.id === work.id ? { ...w, isPublished: res.data?.isPublished } : w))
      );
      setStatusMessage({
        type: 'success',
        text: `Work '${work.title}' ${targetStatus ? 'published' : 'set to draft'} successfully.`,
      });
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || 'Failed to update publish status.' });
    }
  };

  // Delete Work
  const handleDeleteWork = async (work) => {
    if (
      !window.confirm(
        `Are you sure you want to delete '${work.title}'? This will permanently remove its cover and gallery images from Cloudinary and database.`
      )
    ) {
      return;
    }

    try {
      await fetchApi(`/admin/works/${work.id}`, {
        method: 'DELETE',
      });
      setWorksList((prev) => prev.filter((w) => w.id !== work.id));
      setStatusMessage({ type: 'success', text: `Work '${work.title}' deleted successfully.` });
    } catch (err) {
      setStatusMessage({ type: 'error', text: err.message || 'Failed to delete portfolio work.' });
    }
  };

  // --- UNAUTHENTICATED LOGIN SCREEN ---
  if (!authenticated) {
    return (
      <div className="section-padding container" style={{ maxWidth: '460px', marginTop: '6rem' }}>
        <SeoHead title="Admin Login | Smile Studios" description="Smile Studios Admin Authentication Gate" />
        <div
          style={{
            background: 'var(--bg-surface)',
            border: '1px solid var(--border-light)',
            padding: '3rem 2rem',
            borderRadius: 'var(--radius-md)',
            textAlign: 'center',
            boxShadow: '0 10px 30px rgba(0,0,0,0.5)',
          }}
        >
          <div
            style={{
              width: '64px',
              height: '64px',
              borderRadius: '50%',
              background: 'rgba(212, 175, 55, 0.1)',
              border: '1px solid var(--accent-gold)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
            }}
          >
            <Lock size={28} color="var(--accent-gold)" />
          </div>

          <h2 style={{ fontSize: '1.8rem', marginBottom: '0.5rem', color: 'var(--text-main)' }}>Smile Studios Admin</h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '2rem' }}>
            Enter your admin credentials to log into the management portal.
          </p>

          {statusMessage && (
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
                padding: '0.8rem 1rem',
                borderRadius: '4px',
                marginBottom: '1.5rem',
                background: statusMessage.type === 'success' ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)',
                border: `1px solid ${statusMessage.type === 'success' ? '#22c55e' : '#ef4444'}`,
                color: statusMessage.type === 'success' ? '#22c55e' : '#ef4444',
                fontSize: '0.85rem',
                textAlign: 'left',
              }}
            >
              <AlertCircle size={18} />
              <span>{statusMessage.text}</span>
            </div>
          )}

          <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '1.2rem' }}>
            <div className="form-group" style={{ textAlign: 'left' }}>
              <label style={{ fontSize: '0.8rem' }}>Admin Email</label>
              <input
                type="email"
                className="form-input"
                placeholder="admin@smilestudios.com"
                value={loginEmail}
                onChange={(e) => setLoginEmail(e.target.value)}
                required
              />
            </div>

            <div className="form-group" style={{ textAlign: 'left' }}>
              <label style={{ fontSize: '0.8rem' }}>Password</label>
              <input
                type="password"
                className="form-input"
                placeholder="••••••••••••"
                value={loginPassword}
                onChange={(e) => setLoginPassword(e.target.value)}
                required
              />
            </div>

            <button type="submit" className="btn-primary" disabled={loggingIn} style={{ justifyContent: 'center', marginTop: '0.5rem' }}>
              {loggingIn ? 'Authenticating...' : 'Sign In to Dashboard'} <Key size={16} />
            </button>
          </form>
        </div>
      </div>
    );
  }

  // --- AUTHENTICATED ADMIN DASHBOARD ---
  return (
    <>
      <SeoHead title="Admin Dashboard | Smile Studios" description="Manage portfolio works, Cloudinary images, and placements." />

      <div className="page-header">
        <div className="container">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <span className="subtitle">SMILE STUDIOS ADMIN</span>
              <h1>Portfolio Work & Image Control</h1>
              {userInfo && (
                <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '0.25rem' }}>
                  Logged in as: <strong style={{ color: 'var(--accent-gold)' }}>{userInfo.email}</strong> ({userInfo.role})
                </p>
              )}
            </div>

            <button
              onClick={() => handleLogout()}
              className="btn-outline"
              style={{ borderColor: '#ef4444', color: '#ef4444', padding: '0.5rem 1.2rem', fontSize: '0.85rem' }}
            >
              <LogOut size={16} /> Sign Out
            </button>
          </div>

          {/* Navigation Tabs */}
          <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', marginTop: '2rem' }}>
            <button
              className={activeTab === 'create' ? 'btn-primary' : 'btn-outline'}
              onClick={() => setActiveTab('create')}
              style={{ padding: '0.6rem 1.5rem', fontSize: '0.85rem' }}
            >
              <Plus size={16} /> Add New Work
            </button>
            <button
              className={activeTab === 'manage' ? 'btn-primary' : 'btn-outline'}
              onClick={() => setActiveTab('manage')}
              style={{ padding: '0.6rem 1.5rem', fontSize: '0.85rem' }}
            >
              <Layers size={16} /> Manage Works ({worksList.length})
            </button>
          </div>
        </div>
      </div>

      <section className="section-padding container">
        {statusMessage && (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.75rem',
              padding: '1rem',
              borderRadius: '4px',
              marginBottom: '2rem',
              background: statusMessage.type === 'success' ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)',
              border: `1px solid ${statusMessage.type === 'success' ? '#22c55e' : '#ef4444'}`,
              color: statusMessage.type === 'success' ? '#22c55e' : '#ef4444',
              fontSize: '0.9rem',
              maxWidth: activeTab === 'create' ? '850px' : '100%',
              margin: '0 auto 2rem',
            }}
          >
            {statusMessage.type === 'success' ? <CheckCircle size={20} /> : <AlertCircle size={20} />}
            <span style={{ flex: 1 }}>{statusMessage.text}</span>
            <X size={18} style={{ cursor: 'pointer' }} onClick={() => setStatusMessage(null)} />
          </div>
        )}

        {/* --- TAB 1: ADD NEW WORK FORM --- */}
        {activeTab === 'create' && (
          <div
            style={{
              background: 'var(--bg-surface)',
              border: '1px solid var(--border-light)',
              padding: '3rem',
              borderRadius: 'var(--radius-md)',
              maxWidth: '850px',
              margin: '0 auto',
            }}
          >
            <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)', marginBottom: '1.5rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
              Add New Portfolio Work
            </h3>

            <form onSubmit={handleCreateWork} style={{ display: 'flex', flexDirection: 'column', gap: '1.8rem' }}>
              {/* Title & Category */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
                <div className="form-group">
                  <label style={{ fontSize: '0.85rem', color: 'var(--text-main)' }}>Work Title *</label>
                  <input
                    type="text"
                    className="form-input"
                    placeholder="e.g. Arun & Priya Wedding"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                  />
                </div>

                <div className="form-group">
                  <label style={{ fontSize: '0.85rem', color: 'var(--text-main)' }}>Category *</label>
                  <select
                    className="form-input"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    required
                  >
                    <option value="" disabled>-- Select Category --</option>
                    {categories.map((cat) => (
                      <option key={cat.slug || cat.name} value={cat.name || cat.slug}>
                        {cat.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Description */}
              <div className="form-group">
                <label style={{ fontSize: '0.85rem', color: 'var(--text-main)' }}>Description (Optional)</label>
                <textarea
                  className="form-input"
                  rows={3}
                  placeholder="Beautiful intimate wedding ceremony at Grand Palace..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  style={{ resize: 'vertical' }}
                />
              </div>

              {/* COVER IMAGE FILE PICKER */}
              <div className="form-group">
                <label style={{ fontSize: '0.85rem', color: 'var(--accent-gold)', fontWeight: 600 }}>Cover Image (Required) *</label>
                <div
                  style={{
                    border: '2px dashed var(--border-light)',
                    borderRadius: 'var(--radius-md)',
                    padding: '2rem 1.5rem',
                    textAlign: 'center',
                    background: 'var(--bg-dark)',
                    cursor: 'pointer',
                    position: 'relative',
                  }}
                  onClick={() => document.getElementById('cover-file-input').click()}
                >
                  {coverPreviewUrl ? (
                    <div>
                      <img src={coverPreviewUrl} alt="Cover Preview" style={{ maxHeight: '220px', margin: '0 auto 1rem', borderRadius: '8px', objectFit: 'cover' }} />
                      <p style={{ fontSize: '0.85rem', color: 'var(--accent-gold)' }}>Click to replace cover image</p>
                    </div>
                  ) : (
                    <div>
                      <ImageIcon size={44} color="var(--accent-gold)" style={{ marginBottom: '0.75rem' }} />
                      <h4 style={{ color: 'var(--text-main)', fontSize: '1rem', marginBottom: '0.25rem' }}>Select Cover Photo</h4>
                      <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>PNG, JPG, WEBP up to 10MB</p>
                    </div>
                  )}
                  <input
                    type="file"
                    id="cover-file-input"
                    accept="image/*"
                    onChange={handleCoverChange}
                    style={{ display: 'none' }}
                  />
                </div>
              </div>

              {/* MULTIPLE GALLERY IMAGES PICKER */}
              <div className="form-group">
                <label style={{ fontSize: '0.85rem', color: 'var(--text-main)' }}>Gallery Images (Multiple Optional)</label>
                <div
                  style={{
                    border: '1px dashed var(--border-light)',
                    borderRadius: 'var(--radius-md)',
                    padding: '1.5rem',
                    background: 'var(--bg-dark)',
                    textAlign: 'center',
                    cursor: 'pointer',
                  }}
                  onClick={() => document.getElementById('gallery-file-input').click()}
                >
                  <Upload size={32} color="var(--accent-gold)" style={{ marginBottom: '0.5rem' }} />
                  <p style={{ fontSize: '0.85rem', color: 'var(--text-main)' }}>Click to select multiple gallery photos</p>
                  <input
                    type="file"
                    id="gallery-file-input"
                    accept="image/*"
                    multiple
                    onChange={handleGalleryChange}
                    style={{ display: 'none' }}
                  />
                </div>

                {/* Previews Grid for Selected Gallery Images */}
                {galleryPreviews.length > 0 && (
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(100px, 1fr))', gap: '0.75rem', marginTop: '1rem' }}>
                    {galleryPreviews.map((url, index) => (
                      <div key={url} style={{ position: 'relative', height: '100px', borderRadius: '6px', overflow: 'hidden', border: '1px solid var(--border-light)' }}>
                        <img src={url} alt={`Gallery preview ${index}`} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                        <button
                          type="button"
                          onClick={() => removeGalleryImage(index)}
                          style={{
                            position: 'absolute',
                            top: '4px',
                            right: '4px',
                            background: 'rgba(239, 68, 68, 0.85)',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '50%',
                            width: '22px',
                            height: '22px',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            cursor: 'pointer',
                          }}
                        >
                          <X size={14} />
                        </button>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* Display & Placement Flags */}
              <div style={{ background: 'var(--bg-dark)', padding: '1.5rem', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border-light)' }}>
                <h4 style={{ fontSize: '0.9rem', color: 'var(--accent-gold)', marginBottom: '1rem', textTransform: 'uppercase', letterSpacing: '0.08em' }}>
                  Visibility & Display Placements
                </h4>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)', fontSize: '0.9rem' }}>
                    <input
                      type="checkbox"
                      checked={isPublished}
                      onChange={(e) => setIsPublished(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Published (Visible on Public Website)
                  </label>

                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)', fontSize: '0.9rem' }}>
                    <input
                      type="checkbox"
                      checked={isFeatured}
                      onChange={(e) => setIsFeatured(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Featured Work
                  </label>

                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)', fontSize: '0.9rem' }}>
                    <input
                      type="checkbox"
                      checked={showInHero}
                      onChange={(e) => setShowInHero(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Show in Hero Slideshow
                  </label>

                  <label style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer', color: 'var(--text-main)', fontSize: '0.9rem' }}>
                    <input
                      type="checkbox"
                      checked={showInSelectedWorks}
                      onChange={(e) => setShowInSelectedWorks(e.target.checked)}
                      style={{ width: '18px', height: '18px', accentColor: 'var(--accent-gold)' }}
                    />
                    Show in Selected Works Grid
                  </label>
                </div>
              </div>

              {/* Display Order */}
              <div className="form-group" style={{ maxWidth: '200px' }}>
                <label style={{ fontSize: '0.85rem' }}>Display Order #</label>
                <input
                  type="number"
                  className="form-input"
                  value={displayOrder}
                  onChange={(e) => setDisplayOrder(Number(e.target.value))}
                  min="0"
                />
              </div>

              <button
                type="submit"
                className="btn-primary"
                disabled={creating}
                style={{ justifyContent: 'center', marginTop: '1rem', padding: '0.8rem' }}
              >
                {creating ? 'Uploading & Creating Work...' : 'Create Portfolio Work'} <Upload size={18} />
              </button>
            </form>
          </div>
        )}

        {/* --- TAB 2: MANAGE WORKS LIST & EDIT --- */}
        {activeTab === 'manage' && (
          <div style={{ background: 'var(--bg-surface)', border: '1px solid var(--border-light)', padding: '2rem', borderRadius: 'var(--radius-md)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
              <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)' }}>
                All Portfolio Works ({worksList.length})
              </h3>
              <button className="btn-outline" onClick={loadAdminWorks} style={{ padding: '0.4rem 1rem', fontSize: '0.8rem' }}>
                Refresh List
              </button>
            </div>

            {loadingWorks ? (
              <p style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>Loading portfolio works list...</p>
            ) : worksList.length === 0 ? (
              <p style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>No portfolio works created yet.</p>
            ) : (
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '1.5rem' }}>
                {worksList.map((work) => (
                  <div
                    key={work.id}
                    style={{
                      background: 'var(--bg-dark)',
                      border: '1px solid var(--border-light)',
                      borderRadius: 'var(--radius-md)',
                      overflow: 'hidden',
                      display: 'flex',
                      flexDirection: 'column',
                    }}
                  >
                    {/* Cover Thumbnail */}
                    <div style={{ position: 'relative', height: '180px', background: '#000' }}>
                      <img
                        src={work.coverImageUrl}
                        alt={work.title}
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      />
                      <div
                        style={{
                          position: 'absolute',
                          top: '10px',
                          right: '10px',
                          display: 'flex',
                          gap: '6px',
                        }}
                      >
                        <span
                          style={{
                            padding: '3px 8px',
                            borderRadius: '4px',
                            fontSize: '0.75rem',
                            fontWeight: 600,
                            background: work.isPublished ? 'rgba(34, 197, 94, 0.9)' : 'rgba(107, 114, 128, 0.9)',
                            color: '#fff',
                          }}
                        >
                          {work.isPublished ? 'Published' : 'Draft'}
                        </span>
                        {work.isFeatured && (
                          <span
                            style={{
                              padding: '3px 8px',
                              borderRadius: '4px',
                              fontSize: '0.75rem',
                              fontWeight: 600,
                              background: 'var(--accent-gold)',
                              color: '#000',
                            }}
                          >
                            Featured
                          </span>
                        )}
                      </div>
                    </div>

                    {/* Details */}
                    <div style={{ padding: '1.25rem', flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                      <div>
                        <span style={{ fontSize: '0.75rem', color: 'var(--accent-gold)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                          {work.category || 'Unassigned'}
                        </span>
                        <h4 style={{ fontSize: '1.1rem', color: 'var(--text-main)', margin: '0.25rem 0 0.5rem' }}>{work.title}</h4>
                        <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '1rem', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                          {work.description || 'No description provided.'}
                        </p>
                      </div>

                      <div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: '1rem' }}>
                          Gallery Images: <strong>{work.images?.length || 0}</strong> | Order: <strong>#{work.displayOrder}</strong>
                        </div>

                        {/* Action Buttons */}
                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                          <button
                            className="btn-primary"
                            onClick={() => openEditModal(work)}
                            style={{ flex: 1, padding: '0.4rem 0.6rem', fontSize: '0.75rem', justifyContent: 'center' }}
                          >
                            <Edit size={14} /> Edit
                          </button>

                          <button
                            className="btn-outline"
                            onClick={() => handleTogglePublish(work)}
                            style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem' }}
                            title={work.isPublished ? 'Unpublish' : 'Publish'}
                          >
                            {work.isPublished ? <EyeOff size={14} /> : <Eye size={14} />}
                          </button>

                          <button
                            className="btn-outline"
                            onClick={() => handleDeleteWork(work)}
                            style={{ padding: '0.4rem 0.6rem', fontSize: '0.75rem', borderColor: '#ef4444', color: '#ef4444' }}
                            title="Delete Work"
                          >
                            <Trash2 size={14} />
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </section>

      {/* --- EDIT WORK MODAL --- */}
      {editingWork && (
        <div
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            background: 'rgba(0,0,0,0.8)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            padding: '2rem',
          }}
        >
          <div
            style={{
              background: 'var(--bg-surface)',
              border: '1px solid var(--border-light)',
              borderRadius: 'var(--radius-md)',
              width: '100%',
              maxWidth: '800px',
              maxHeight: '90vh',
              overflowY: 'auto',
              padding: '2rem',
              position: 'relative',
            }}
          >
            <button
              onClick={closeEditModal}
              style={{
                position: 'absolute',
                top: '1.5rem',
                right: '1.5rem',
                background: 'none',
                border: 'none',
                color: 'var(--text-muted)',
                cursor: 'pointer',
              }}
            >
              <X size={24} />
            </button>

            <h3 style={{ fontSize: '1.4rem', color: 'var(--text-main)', marginBottom: '1.5rem' }}>
              Edit Portfolio Work: {editingWork.title}
            </h3>

            <form onSubmit={handleUpdateWork} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label style={{ fontSize: '0.8rem' }}>Title *</label>
                  <input
                    type="text"
                    className="form-input"
                    value={editTitle}
                    onChange={(e) => setEditTitle(e.target.value)}
                    required
                  />
                </div>

                <div className="form-group">
                  <label style={{ fontSize: '0.8rem' }}>Category *</label>
                  <select
                    className="form-input"
                    value={editCategory}
                    onChange={(e) => setEditCategory(e.target.value)}
                    required
                  >
                    {categories.map((cat) => (
                      <option key={cat.slug || cat.name} value={cat.name || cat.slug}>
                        {cat.name}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label style={{ fontSize: '0.8rem' }}>Description</label>
                <textarea
                  className="form-input"
                  rows={3}
                  value={editDescription}
                  onChange={(e) => setEditDescription(e.target.value)}
                />
              </div>

              {/* Replace Cover Image */}
              <div className="form-group">
                <label style={{ fontSize: '0.8rem' }}>Cover Image (Select file to replace current cover)</label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => {
                    const file = e.target.files[0];
                    if (file) {
                      if (newCoverPreviewUrl) URL.revokeObjectURL(newCoverPreviewUrl);
                      setNewCoverFile(file);
                      setNewCoverPreviewUrl(URL.createObjectURL(file));
                    }
                  }}
                  className="form-input"
                />
                {(newCoverPreviewUrl || editingWork.coverImageUrl) && (
                  <div style={{ marginTop: '0.5rem' }}>
                    <img
                      src={newCoverPreviewUrl || editingWork.coverImageUrl}
                      alt="Cover"
                      style={{ maxHeight: '120px', borderRadius: '6px' }}
                    />
                  </div>
                )}
              </div>

              {/* Existing Gallery Images List */}
              {existingImages.length > 0 && (
                <div className="form-group">
                  <label style={{ fontSize: '0.8rem', color: 'var(--accent-gold)' }}>Existing Gallery Images ({existingImages.length})</label>
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(90px, 1fr))', gap: '0.5rem', marginTop: '0.5rem' }}>
                    {existingImages.map((img) => (
                      <div key={img.id} style={{ position: 'relative', height: '80px', borderRadius: '6px', overflow: 'hidden', border: '1px solid var(--border-light)' }}>
                        <img src={img.imageUrl} alt={img.altText || 'Gallery'} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                        <button
                          type="button"
                          onClick={() => handleDeleteExistingImage(img.id)}
                          style={{
                            position: 'absolute',
                            top: '4px',
                            right: '4px',
                            background: 'rgba(239, 68, 68, 0.9)',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '50%',
                            width: '20px',
                            height: '20px',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            cursor: 'pointer',
                          }}
                          title="Delete image from work"
                        >
                          <X size={12} />
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Add New Gallery Images */}
              <div className="form-group">
                <label style={{ fontSize: '0.8rem' }}>Add More Gallery Images</label>
                <input
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={(e) => {
                    const files = Array.from(e.target.files);
                    if (files.length > 0) {
                      setNewGalleryFiles((prev) => [...prev, ...files]);
                      setNewGalleryPreviews((prev) => [...prev, ...files.map((f) => URL.createObjectURL(f))]);
                    }
                  }}
                  className="form-input"
                />
              </div>

              {/* Flags & Orders */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.8rem' }}>
                <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer', fontSize: '0.85rem' }}>
                  <input
                    type="checkbox"
                    checked={editIsPublished}
                    onChange={(e) => setEditIsPublished(e.target.checked)}
                    style={{ accentColor: 'var(--accent-gold)' }}
                  />
                  Published
                </label>

                <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer', fontSize: '0.85rem' }}>
                  <input
                    type="checkbox"
                    checked={editIsFeatured}
                    onChange={(e) => setEditIsFeatured(e.target.checked)}
                    style={{ accentColor: 'var(--accent-gold)' }}
                  />
                  Featured
                </label>

                <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer', fontSize: '0.85rem' }}>
                  <input
                    type="checkbox"
                    checked={editShowInHero}
                    onChange={(e) => setEditShowInHero(e.target.checked)}
                    style={{ accentColor: 'var(--accent-gold)' }}
                  />
                  Show in Hero
                </label>

                <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer', fontSize: '0.85rem' }}>
                  <input
                    type="checkbox"
                    checked={editShowInSelectedWorks}
                    onChange={(e) => setEditShowInSelectedWorks(e.target.checked)}
                    style={{ accentColor: 'var(--accent-gold)' }}
                  />
                  Show in Selected Works
                </label>
              </div>

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '1rem', marginTop: '1rem' }}>
                <button type="button" className="btn-outline" onClick={closeEditModal}>
                  Cancel
                </button>
                <button type="submit" className="btn-primary" disabled={updating}>
                  {updating ? 'Saving...' : 'Save Changes'} <Save size={16} />
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}
