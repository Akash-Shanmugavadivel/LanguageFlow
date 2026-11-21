import React, { useState } from 'react';
import {
  Container, Typography, Card, CardContent, Button, Box,
  TextField, Alert, CircularProgress, Grid, Chip
} from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech';
import WorkspacePremiumIcon from '@mui/icons-material/WorkspacePremium';
import StarIcon from '@mui/icons-material/Star';
import SaveIcon from '@mui/icons-material/Save';
import { useAuth } from '../context/AuthContext';
import { useProgress } from '../context/ProgressContext';
import StatCard from '../components/common/StatCard';

interface Badge {
  id: string;
  name: string;
  description: string;
  earned: boolean;
  icon: React.ReactNode;
}

export const ProfilePage: React.FC = () => {
  const { user } = useAuth();
  const { streak, dailyGoalMinutes, overallProgress, updateDailyGoal } = useProgress();

  const [newGoal, setNewGoal] = useState<number>(dailyGoalMinutes || 15);
  const [savingGoal, setSavingGoal] = useState<boolean>(false);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Gamification: Earned badges evaluated against user's real progress and streak
  const badges: Badge[] = [
    {
      id: 'first_step',
      name: 'First Steps',
      description: 'Enrolled and completed your first lesson',
      earned: overallProgress > 0,
      icon: <StarIcon sx={{ color: '#FFD700' }} />
    },
    {
      id: 'streak_3',
      name: 'On Fire (3-Day Streak)',
      description: 'Studied 3 days in a row',
      earned: streak >= 3,
      icon: <LocalFireDepartmentIcon sx={{ color: '#FF5722' }} />
    },
    {
      id: 'halfway',
      name: 'Halfway Explorer',
      description: 'Reached 50% overall curriculum completion',
      earned: overallProgress >= 50,
      icon: <MilitaryTechIcon sx={{ color: '#2196F3' }} />
    },
    {
      id: 'polyglot',
      name: 'Language Master',
      description: 'Completed 100% of curriculum and passed quizzes',
      earned: overallProgress >= 100,
      icon: <WorkspacePremiumIcon sx={{ color: '#9C27B0' }} />
    }
  ];

  const handleUpdateGoal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newGoal <= 0) {
      setError('Daily goal must be at least 1 minute.');
      return;
    }
    setSavingGoal(true);
    setError(null);
    setMessage(null);
    const ok = await updateDailyGoal(newGoal);
    if (ok) setMessage('Daily goal updated!');
    else setError('Failed to update daily goal.');
    setSavingGoal(false);
  };

  return (
    <Container maxWidth="md" sx={{ mt: 5, mb: 6 }}>
      {/* Profile Header */}
      <Card sx={{ p: 3, mb: 4, borderRadius: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Box sx={{ p: 1.5, borderRadius: '50%', bgcolor: 'primary.light', color: 'white' }}>
            <AccountCircleIcon sx={{ fontSize: 48 }} />
          </Box>
          <Box>
            <Typography variant="h5" fontWeight={800}>{user?.username || 'Learner'}</Typography>
            <Typography variant="body2" color="text.secondary">Active LanguageFlow Member</Typography>
          </Box>
        </Box>
      </Card>

      {/* Overview Metrics */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={4}>
          <StatCard
            title="Learning Streak"
            value={`${streak} Days`}
            subtitle="Keep practicing daily"
            icon={<LocalFireDepartmentIcon sx={{ fontSize: 32 }} />}
            color="#FF9800"
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <StatCard
            title="Daily Target"
            value={`${dailyGoalMinutes} min`}
            subtitle="Target practice per day"
            icon={<AccessTimeIcon sx={{ fontSize: 32 }} />}
            color="#1E3C78"
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <StatCard
            title="Overall Progress"
            value={`${Math.round(overallProgress)}%`}
            subtitle="Curriculum completion"
            icon={<TrendingUpIcon sx={{ fontSize: 32 }} />}
            color="#2E7D32"
          />
        </Grid>
      </Grid>

      {/* Earned Badges Section */}
      <Card sx={{ borderRadius: 3, p: 3, mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
          <EmojiEventsIcon color="primary" />
          <Typography variant="h6" fontWeight={700}>Earned Badges</Typography>
        </Box>
        <Grid container spacing={2}>
          {badges.map((b) => (
            <Grid item xs={12} sm={6} key={b.id}>
              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 1.5,
                  p: 2,
                  borderRadius: 2,
                  border: '1px solid',
                  borderColor: b.earned ? 'success.light' : 'divider',
                  bgcolor: b.earned ? 'action.hover' : 'background.paper',
                  opacity: b.earned ? 1 : 0.55
                }}
              >
                {b.icon}
                <Box sx={{ flex: 1 }}>
                  <Typography variant="subtitle2" fontWeight={700}>{b.name}</Typography>
                  <Typography variant="caption" color="text.secondary">{b.description}</Typography>
                </Box>
                <Chip
                  label={b.earned ? 'Earned' : 'Locked'}
                  size="small"
                  color={b.earned ? 'success' : 'default'}
                  variant={b.earned ? 'filled' : 'outlined'}
                />
              </Box>
            </Grid>
          ))}
        </Grid>
      </Card>

      {/* Preferences / Daily Target */}
      <Card sx={{ borderRadius: 3, p: 3 }}>
        <Typography variant="h6" fontWeight={700} gutterBottom>Daily Goal Settings</Typography>
        {message && <Alert severity="success" sx={{ mb: 2 }}>{message}</Alert>}
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <Box component="form" onSubmit={handleUpdateGoal} sx={{ maxWidth: 360, display: 'flex', gap: 2, mt: 1 }}>
          <TextField
            label="Minutes per day"
            type="number"
            size="small"
            value={newGoal}
            onChange={(e) => setNewGoal(Math.max(1, parseInt(e.target.value) || 0))}
            inputProps={{ min: 1, max: 240 }}
            disabled={savingGoal}
          />
          <Button type="submit" variant="contained" startIcon={<SaveIcon />} disabled={savingGoal}>
            Save
          </Button>
        </Box>
      </Card>
    </Container>
  );
};

export default ProfilePage;
