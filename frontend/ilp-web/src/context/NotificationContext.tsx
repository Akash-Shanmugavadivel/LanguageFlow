import React, { createContext, useContext, useState, ReactNode, useCallback } from 'react';
import Snackbar from '@mui/material/Snackbar';
import Alert, { AlertColor } from '@mui/material/Alert';

interface NotificationContextType {
  showNotification: (message: string, severity?: AlertColor) => void;
  showError: (message: string) => void;
  showSuccess: (message: string) => void;
  showInfo: (message: string) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

// Standalone trigger for axios interceptor outside React tree
let notifyHandler: ((message: string, severity: AlertColor) => void) | null = null;

export const notifyGlobal = (message: string, severity: AlertColor = 'error') => {
  if (notifyHandler) {
    notifyHandler(message, severity);
  }
};

export const NotificationProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState<AlertColor>('error');

  const showNotification = useCallback((msg: string, sev: AlertColor = 'error') => {
    setMessage(msg);
    setSeverity(sev);
    setOpen(true);
  }, []);

  const showError = useCallback((msg: string) => showNotification(msg, 'error'), [showNotification]);
  const showSuccess = useCallback((msg: string) => showNotification(msg, 'success'), [showNotification]);
  const showInfo = useCallback((msg: string) => showNotification(msg, 'info'), [showNotification]);

  // Bind to global handler
  notifyHandler = showNotification;

  const handleClose = (_event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') return;
    setOpen(false);
  };

  return (
    <NotificationContext.Provider value={{ showNotification, showError, showSuccess, showInfo }}>
      {children}
      <Snackbar
        open={open}
        autoHideDuration={4500}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert onClose={handleClose} severity={severity} variant="filled" sx={{ width: '100%', boxShadow: 3 }}>
          {message}
        </Alert>
      </Snackbar>
    </NotificationContext.Provider>
  );
};

export const useNotification = (): NotificationContextType => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotification must be used within NotificationProvider');
  }
  return context;
};
