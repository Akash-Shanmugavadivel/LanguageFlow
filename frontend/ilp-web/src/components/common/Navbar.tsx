import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Container from '@mui/material/Container';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import SchoolIcon from '@mui/icons-material/School';
import LogoutIcon from '@mui/icons-material/Logout';
import LoginIcon from '@mui/icons-material/Login';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { useAuth } from '../../context/AuthContext';
import { useProgress } from '../../context/ProgressContext';
import { useNavigate, Link as RouterLink } from 'react-router-dom';

export const Navbar: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const { streak } = useProgress();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <AppBar position="sticky" sx={{ background: 'linear-gradient(135deg, #1E3C78 0%, #2A5298 100%)' }}>
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ justifyContent: 'space-between' }}>
          {/* Brand Logo */}
          <Box
            sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}
            onClick={() => navigate(isAuthenticated ? '/courses' : '/')}
          >
            <SchoolIcon sx={{ mr: 1, fontSize: 32, color: '#ADD8E6' }} />
            <Typography
              variant="h6"
              noWrap
              sx={{
                fontWeight: 700,
                letterSpacing: '.1rem',
                color: '#FFFFFF',
                textDecoration: 'none',
              }}
            >
              LanguageFlow
            </Typography>
          </Box>

          {/* Navigation & User Menu */}
          {isAuthenticated ? (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              {/* Learning Streak Pill */}
              <Chip
                icon={<LocalFireDepartmentIcon sx={{ color: '#FF9800 !important' }} />}
                label={`${streak} Day Streak`}
                sx={{
                  backgroundColor: 'rgba(255, 255, 255, 0.15)',
                  color: '#FFFFFF',
                  fontWeight: 600,
                  fontSize: '0.85rem',
                  border: '1px solid rgba(255, 255, 255, 0.25)',
                }}
              />

              <Button
                color="inherit"
                onClick={() => navigate('/courses')}
                sx={{ color: '#FFFFFF', fontWeight: 600 }}
              >
                Courses
              </Button>

              <Button
                color="inherit"
                startIcon={<AccountCircleIcon />}
                onClick={() => navigate('/profile')}
                sx={{ color: '#FFFFFF', fontWeight: 600 }}
              >
                {user?.username || 'Profile'}
              </Button>

              <Button
                variant="outlined"
                size="small"
                startIcon={<LogoutIcon />}
                onClick={handleLogout}
                sx={{
                  color: '#ADD8E6',
                  borderColor: '#ADD8E6',
                  '&:hover': {
                    borderColor: '#FFFFFF',
                    backgroundColor: 'rgba(173, 216, 230, 0.1)',
                  },
                }}
              >
                Logout
              </Button>
            </Box>
          ) : (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <Button
                component={RouterLink}
                to="/login"
                color="inherit"
                startIcon={<LoginIcon />}
                sx={{ color: '#FFFFFF', fontWeight: 600 }}
              >
                Sign In
              </Button>
              <Button
                component={RouterLink}
                to="/register"
                variant="contained"
                size="small"
                startIcon={<PersonAddIcon />}
                sx={{
                  bgcolor: '#ADD8E6',
                  color: '#1E3C78',
                  fontWeight: 700,
                  '&:hover': { bgcolor: '#FFFFFF' },
                }}
              >
                Register
              </Button>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Navbar;
