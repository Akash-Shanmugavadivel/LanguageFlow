import React, { useState, useEffect } from 'react';
import {
  Container, Typography, Grid, Card, CardContent, CardActions,
  Button, Box, CircularProgress, Alert, LinearProgress
} from '@mui/material';
import SchoolIcon from '@mui/icons-material/School';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/client';
import { Course, ApiResponse, CourseProgress } from '../types';
import { useProgress } from '../context/ProgressContext';

export const CoursesPage: React.FC = () => {
  const navigate = useNavigate();
  const { courseProgressMap, fetchCourseProgress } = useProgress();
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const res = await apiClient.get<ApiResponse<Course[]>>('/courses');
        if (res.data?.data) {
          setCourses(res.data.data);
          res.data.data.forEach((c) => fetchCourseProgress(c.id));
        }
      } catch (err: any) {
        setError(err.response?.data?.message || err.message || 'Failed to load courses');
      } finally {
        setLoading(false);
      }
    })();
  }, [fetchCourseProgress]);

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 6 }}>
      <Typography variant="h4" fontWeight={800} gutterBottom>Language Courses</Typography>
      {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}
      <Grid container spacing={3}>
        {courses.map((course) => {
          const progress: CourseProgress | undefined = courseProgressMap[course.id];
          const pct = progress ? Math.round(progress.progressPercentage) : 0;
          const completed = progress?.completedLessons ?? 0;
          return (
            <Grid item xs={12} sm={6} md={4} key={course.id}>
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderRadius: 3 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mb: 1.5 }}>
                    <Box sx={{ p: 1, bgcolor: 'primary.light', color: 'white', borderRadius: 2 }}><SchoolIcon /></Box>
                    <Typography variant="h6" fontWeight={700}>{course.name}</Typography>
                  </Box>
                  <Typography variant="body2" color="text.secondary" sx={{ minHeight: 48, mb: 2 }}>{course.description}</Typography>
                  <Box sx={{ mb: 1 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
                      <Typography variant="caption" fontWeight={600}>Progress ({completed}/{course.totalLessons})</Typography>
                      <Typography variant="caption" fontWeight={700} color="primary.main">{pct}%</Typography>
                    </Box>
                    <LinearProgress variant="determinate" value={pct} sx={{ height: 8, borderRadius: 4 }} />
                  </Box>
                </CardContent>
                <CardActions sx={{ px: 2, pb: 2 }}>
                  <Button
                    variant="contained"
                    fullWidth
                    startIcon={<PlayArrowIcon />}
                    onClick={() => navigate(`/lessons/${course.id}`)}
                    sx={{ fontWeight: 700 }}
                  >
                    Start
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          );
        })}
      </Grid>
    </Container>
  );
};

export default CoursesPage;
