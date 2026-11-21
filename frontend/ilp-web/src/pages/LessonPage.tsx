import React, { useState, useEffect } from 'react';
import {
  Container, Typography, Card, CardContent, Button, Box,
  CircularProgress, Alert, Divider, Chip, Paper, List, ListItem, ListItemButton, ListItemText, ListItemIcon
} from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import RadioButtonUncheckedIcon from '@mui/icons-material/RadioButtonUnchecked';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import QuizIcon from '@mui/icons-material/Quiz';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import HistoryIcon from '@mui/icons-material/History';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/client';
import { Lesson, Course, ApiResponse, CourseProgress } from '../types';
import { useProgress } from '../context/ProgressContext';

export const LessonPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { completeLesson, courseProgressMap, fetchCourseProgress } = useProgress();

  const courseId = Number(id);
  const [course, setCourse] = useState<Course | null>(null);
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [currentIdx, setCurrentIdx] = useState<number>(0);
  const [lastViewed, setLastViewed] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [completing, setCompleting] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      if (!courseId) return;
      try {
        setLoading(true);
        const [cRes, lRes] = await Promise.all([
          apiClient.get<ApiResponse<Course>>(`/courses/${courseId}`),
          apiClient.get<ApiResponse<Lesson[]>>(`/lessons/course/${courseId}`)
        ]);
        if (cRes.data?.data) setCourse(cRes.data.data);
        if (lRes.data?.data) {
          const list = lRes.data.data;
          setLessons(list);
          // Track and restore "last viewed"
          const savedLast = localStorage.getItem(`last_viewed_course_${courseId}`);
          if (savedLast) {
            const foundIdx = list.findIndex(l => String(l.id) === savedLast);
            if (foundIdx >= 0) setCurrentIdx(foundIdx);
          }
        }
        await fetchCourseProgress(courseId);
      } catch (err: any) {
        setError(err.response?.data?.message || err.message || 'Failed to load lesson');
      } finally {
        setLoading(false);
      }
    })();
  }, [courseId, fetchCourseProgress]);

  // Track "last viewed" on lesson change
  useEffect(() => {
    if (lessons[currentIdx]) {
      const lesson = lessons[currentIdx];
      const timeStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      localStorage.setItem(`last_viewed_course_${courseId}`, String(lesson.id));
      localStorage.setItem(`last_viewed_time_course_${courseId}`, timeStr);
      setLastViewed(timeStr);
    }
  }, [currentIdx, lessons, courseId]);

  const currentLesson: Lesson | undefined = lessons[currentIdx];
  const progress: CourseProgress | undefined = courseProgressMap[courseId];
  const isDone = currentLesson ? (progress?.completedLessonIds || []).includes(currentLesson.id) : false;

  const handleComplete = async () => {
    if (!currentLesson) return;
    setCompleting(true);
    await completeLesson(currentLesson.id, courseId);
    setCompleting(false);
  };

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;
  if (!currentLesson) return <Container sx={{ mt: 4 }}><Alert severity="info">No lessons found.</Alert></Container>;

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 6 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/courses')}>Courses</Button>
        <Button variant="outlined" startIcon={<QuizIcon />} onClick={() => navigate(`/quiz/${courseId}`)}>Take Quiz</Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Box sx={{ display: 'flex', gap: 3, flexDirection: { xs: 'column', md: 'row' } }}>
        <Box sx={{ flex: 1 }}>
          <Card sx={{ borderRadius: 3 }}>
            <CardContent sx={{ p: 4 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Chip icon={<MenuBookIcon />} label={`Lesson ${currentIdx + 1} of ${lessons.length}`} color="primary" variant="outlined" size="small" />
                {lastViewed && (
                  <Chip icon={<HistoryIcon />} label={`Viewed: ${lastViewed}`} size="small" variant="outlined" />
                )}
                {isDone && <Chip icon={<CheckCircleIcon />} label="Completed" color="success" size="small" />}
              </Box>

              <Typography variant="h4" fontWeight={800} gutterBottom>{currentLesson.title}</Typography>
              <Divider sx={{ my: 2 }} />

              {/* Render content: preserves paragraphs and styling safely */}
              <Box sx={{ minHeight: 180, py: 1, typography: 'body1', lineHeight: 1.8, whiteSpace: 'pre-line' }}>
                {currentLesson.content}
              </Box>

              <Divider sx={{ my: 3 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Button variant="outlined" startIcon={<ArrowBackIcon />} disabled={currentIdx === 0} onClick={() => setCurrentIdx(i => i - 1)}>Prev</Button>
                <Button variant="contained" color={isDone ? 'inherit' : 'success'} onClick={handleComplete} disabled={completing}>
                  {isDone ? 'Completed' : completing ? 'Saving...' : 'Mark Completed'}
                </Button>
                <Button variant="outlined" endIcon={<ArrowForwardIcon />} disabled={currentIdx >= lessons.length - 1} onClick={() => setCurrentIdx(i => i + 1)}>Next</Button>
              </Box>
            </CardContent>
          </Card>
        </Box>

        <Paper sx={{ width: { xs: '100%', md: 300 }, p: 2, height: 'fit-content', borderRadius: 3 }}>
          <Typography variant="subtitle1" fontWeight={700} gutterBottom>{course?.name || 'Lessons'}</Typography>
          <List dense disablePadding>
            {lessons.map((l, i) => (
              <ListItem
                key={l.id}
                disablePadding
                sx={{ borderRadius: 1.5 }}
              >
                <ListItemButton
                  onClick={() => setCurrentIdx(i)}
                  sx={{ borderRadius: 1.5, bgcolor: i === currentIdx ? 'action.selected' : 'transparent' }}
                >
                  <ListItemIcon sx={{ minWidth: 32 }}>
                    {(progress?.completedLessonIds || []).includes(l.id) ? (
                      <CheckCircleIcon color="success" fontSize="small" />
                    ) : (
                      <RadioButtonUncheckedIcon color="disabled" fontSize="small" />
                    )}
                  </ListItemIcon>
                  <ListItemText primary={`${i + 1}. ${l.title}`} primaryTypographyProps={{ fontWeight: i === currentIdx ? 700 : 400 }} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Paper>
      </Box>
    </Container>
  );
};

export default LessonPage;
