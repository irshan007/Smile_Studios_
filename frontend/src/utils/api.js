const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export async function fetchApi(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const defaultHeaders = {
    'Accept': 'application/json',
  };

  const token = localStorage.getItem('admin_jwt_token');
  if (token) {
    defaultHeaders['Authorization'] = `Bearer ${token}`;
  }

  // Do not set Content-Type header for FormData (browser sets boundary automatically)
  if (!(options.body instanceof FormData)) {
    defaultHeaders['Content-Type'] = 'application/json';
  }

  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  };

  try {
    const response = await fetch(url, config);
    const rawBody = await response.text();

    let result = {};
    if (rawBody) {
      try {
        result = JSON.parse(rawBody);
      } catch {
        throw new Error(`Invalid API response (${response.status})`);
      }
    }

    if (response.status === 401 && !endpoint.includes('/admin/auth/login')) {
      localStorage.removeItem('admin_jwt_token');
      localStorage.removeItem('admin_user_info');
      throw new Error(result.message || 'Your session has expired. Please log in again.');
    }

    if (!response.ok) {
      throw new Error(result.message || `API Error: ${response.status}`);
    }

    return result;
  } catch (error) {
    if (error.name === 'TypeError') {
      throw new Error('Unable to reach the API server. Is the backend running?');
    }
    console.error(`Fetch error on ${endpoint}:`, error);
    throw error;
  }
}

