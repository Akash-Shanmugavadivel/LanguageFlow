import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import { notifyGlobal } from '../context/NotificationContext';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Request Interceptor: Attach JWT Bearer Token
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => Promise.reject(error)
);

// Response Interceptor: Global Error Toast & 401 Handling
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<any>) => {
    const status = error.response?.status;
    const serverMessage = error.response?.data?.message || error.message;

    if (status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
        notifyGlobal('Session expired. Please sign in again.', 'warning');
        window.location.href = '/login';
      }
    } else if (status === 403) {
      notifyGlobal('Access denied. You do not have permission for this action.', 'error');
    } else if (status === 404) {
      notifyGlobal(serverMessage || 'Requested resource not found.', 'error');
    } else if (status && status >= 500) {
      notifyGlobal('Server error. Please try again later.', 'error');
    } else if (!error.response) {
      notifyGlobal('Network error: Unable to connect to backend server.', 'error');
    } else {
      notifyGlobal(serverMessage || 'An unexpected error occurred.', 'error');
    }

    return Promise.reject(error);
  }
);

export default apiClient;
