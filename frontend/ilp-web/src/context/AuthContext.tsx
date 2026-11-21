import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import apiClient from '../api/client';
import { User, ApiResponse } from '../types';

interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  login: (username: string, password: string) => Promise<boolean>;
  register: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  refreshUser: () => Promise<void>;
  clearError: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!token);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchUserProfile = async (username: string): Promise<User | null> => {
    try {
      const response = await apiClient.get<ApiResponse<User>>(`/users/${username}`);
      if (response.data && response.data.data) {
        setUser(response.data.data);
        return response.data.data;
      }
    } catch (err: any) {
      console.error('Failed to fetch user profile', err);
    }
    return null;
  };

  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem('token');
      const storedUsername = localStorage.getItem('username');

      if (storedToken && storedUsername) {
        setToken(storedToken);
        setIsAuthenticated(true);
        await fetchUserProfile(storedUsername);
      } else {
        setToken(null);
        setUser(null);
        setIsAuthenticated(false);
      }
      setIsLoading(false);
    };

    initializeAuth();
  }, []);

  const login = async (username: string, password: string): Promise<boolean> => {
    setError(null);
    setIsLoading(true);
    try {
      const response = await apiClient.post<ApiResponse<{ token: string; username: string }>>(
        '/auth/login',
        { username, password }
      );

      if (response.data.success && response.data.data) {
        const receivedToken = response.data.data.token;
        const validUsername = response.data.data.username;

        localStorage.setItem('token', receivedToken);
        localStorage.setItem('username', validUsername);
        setToken(receivedToken);
        setIsAuthenticated(true);

        await fetchUserProfile(validUsername);
        setIsLoading(false);
        return true;
      } else {
        setError(response.data.message || 'Login failed');
      }
    } catch (err: any) {
      const message = err.response?.data?.message || err.message || 'Invalid credentials';
      setError(message);
    } finally {
      setIsLoading(false);
    }
    return false;
  };

  const register = async (username: string, password: string): Promise<boolean> => {
    setError(null);
    setIsLoading(true);
    try {
      const response = await apiClient.post<ApiResponse<{ token?: string; username: string }>>(
        '/auth/register',
        { username, password }
      );

      if (response.data.success) {
        // If server returns token on register, log in immediately
        if (response.data.data?.token) {
          const receivedToken = response.data.data.token;
          const validUsername = response.data.data.username;
          localStorage.setItem('token', receivedToken);
          localStorage.setItem('username', validUsername);
          setToken(receivedToken);
          setIsAuthenticated(true);
          await fetchUserProfile(validUsername);
        }
        setIsLoading(false);
        return true;
      } else {
        setError(response.data.message || 'Registration failed');
      }
    } catch (err: any) {
      const message = err.response?.data?.message || err.message || 'Registration failed';
      setError(message);
    } finally {
      setIsLoading(false);
    }
    return false;
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
    setError(null);
  };

  const refreshUser = async () => {
    const storedUsername = localStorage.getItem('username');
    if (storedUsername) {
      await fetchUserProfile(storedUsername);
    }
  };

  const clearError = () => setError(null);

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated,
        isLoading,
        error,
        login,
        register,
        logout,
        refreshUser,
        clearError,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
