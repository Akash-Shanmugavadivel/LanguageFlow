import { createTheme } from '@mui/material/styles';

/**
 * Custom Material-UI Theme for LanguageFlow.
 * Reinterprets the original desktop app's aesthetic (Deep Blue & Light Cyan)
 * into a modern, accessible web design system.
 */
export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1E3C78', // Deep Navy / Royal Blue
      light: '#3E5C98',
      dark: '#10244C',
      contrastText: '#FFFFFF',
    },
    secondary: {
      main: '#ADD8E6', // Light Sky Blue accent
      light: '#D4EEF7',
      dark: '#7AAEC0',
      contrastText: '#1E3C78',
    },
    warning: {
      main: '#FF9800', // Streak flame color
    },
    success: {
      main: '#2E7D32',
      light: '#4CAF50',
    },
    background: {
      default: '#F5F8FC',
      paper: '#FFFFFF',
    },
    text: {
      primary: '#1A202C',
      secondary: '#4A5568',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica Neue", "Arial", sans-serif',
    h4: {
      fontWeight: 700,
      color: '#1E3C78',
    },
    h5: {
      fontWeight: 600,
      color: '#1E3C78',
    },
    h6: {
      fontWeight: 600,
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          padding: '8px 20px',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(30, 60, 120, 0.15)',
          },
        },
        containedPrimary: {
          background: 'linear-gradient(135deg, #1E3C78 0%, #2A5298 100%)',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 14,
          boxShadow: '0 4px 20px rgba(0, 0, 0, 0.05)',
          border: '1px solid #E2E8F0',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 10px rgba(0, 0, 0, 0.08)',
          backgroundColor: '#1E3C78',
        },
      },
    },
  },
});

export default theme;
