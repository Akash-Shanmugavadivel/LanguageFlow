import React, { useState, useEffect } from 'react';
import {
  Container, Typography, Card, CardContent, Button, Box,
  CircularProgress, Alert, RadioGroup, FormControlLabel, Radio,
  FormControl, FormLabel, LinearProgress, Chip
} from '@mui/material';
import QuizIcon from '@mui/icons-material/Quiz';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import ReplayIcon from '@mui/icons-material/Replay';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/client';
import { QuizQuestion, QuizResult, ApiResponse } from '../types';
import { useAuth } from '../context/AuthContext';
import { useProgress } from '../context/ProgressContext';

export const QuizPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const courseId = Number(id);
  const navigate = useNavigate();
  const { user } = useAuth();
  const { refreshAllProgress } = useProgress();

  const [questions, setQuestions] = useState<QuizQuestion[]>([]);
  const [currentIdx, setCurrentIdx] = useState<number>(0);
  const [selectedAnswers, setSelectedAnswers] = useState<Record<number, string>>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [result, setResult] = useState<QuizResult | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      if (!courseId) return;
      try {
        setLoading(true);
        const res = await apiClient.get<ApiResponse<QuizQuestion[]>>(`/quizzes/course/${courseId}`);
        if (res.data?.data) setQuestions(res.data.data);
      } catch (err: any) {
        setError(err.response?.data?.message || err.message || 'Failed to fetch quiz');
      } finally {
        setLoading(false);
      }
    })();
  }, [courseId]);

  const handleSelect = (val: string) => {
    const q = questions[currentIdx];
    if (q) setSelectedAnswers(prev => ({ ...prev, [q.id]: val }));
  };

  const handleSubmit = async () => {
    if (!user?.username) return;
    try {
      setSubmitting(true);
      setError(null);
      const res = await apiClient.post<ApiResponse<QuizResult>>(
        `/quizzes/course/${courseId}/submit`,
        { username: user.username, answers: selectedAnswers }
      );
      if (res.data?.data) {
        setResult(res.data.data);
        await refreshAllProgress();
      }
    } catch (err: any) {
      setError(err.response?.data?.message || err.message || 'Submission failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;
  if (questions.length === 0) return <Container sx={{ mt: 4 }}><Alert severity="info">No quiz questions available.</Alert></Container>;

  const currentQ = questions[currentIdx];
  const answeredCount = Object.keys(selectedAnswers).length;
  const progressPct = Math.round(((currentIdx + 1) / questions.length) * 100);

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 6 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/courses')}>Courses</Button>
        <Chip icon={<QuizIcon />} label={`Question ${currentIdx + 1} of ${questions.length}`} color="primary" />
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {/* Immediate Result View */}
      {result ? (
        <Card sx={{ p: 4, textAlign: 'center', borderRadius: 3 }}>
          <CheckCircleIcon sx={{ fontSize: 60, color: result.passed ? 'success.main' : 'warning.main', mb: 2 }} />
          <Typography variant="h4" fontWeight={800} gutterBottom>
            {result.passed ? 'Quiz Passed!' : 'Review & Try Again'}
          </Typography>
          <Typography variant="h6" color="text.secondary">
            Score: {result.score} / {result.totalQuestions} ({Math.round(result.quizPercentage)}%)
          </Typography>
          <Alert severity={result.passed ? 'success' : 'info'} sx={{ my: 3, textAlign: 'left' }}>
            <Typography variant="subtitle2" fontWeight={700}>Explanation & Feedback:</Typography>
            {result.feedbackMessage || (result.passed ? 'Great job mastering the material.' : 'Review earlier lessons and retake.')}
          </Alert>
          <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2 }}>
            <Button variant="outlined" startIcon={<ReplayIcon />} onClick={() => { setResult(null); setCurrentIdx(0); setSelectedAnswers({}); }}>
              Retake Quiz
            </Button>
            <Button variant="contained" onClick={() => navigate('/courses')}>Back to Courses</Button>
          </Box>
        </Card>
      ) : (
        /* One-question-at-a-time Card */
        <Card sx={{ borderRadius: 3, p: 3 }}>
          <LinearProgress variant="determinate" value={progressPct} sx={{ mb: 3, height: 6, borderRadius: 3 }} />
          <CardContent>
            <FormControl component="fieldset" fullWidth>
              <FormLabel component="legend" sx={{ fontWeight: 700, fontSize: '1.2rem', color: 'text.primary', mb: 2 }}>
                {currentIdx + 1}. {currentQ.question}
              </FormLabel>
              <RadioGroup value={selectedAnswers[currentQ.id] || ''} onChange={(e) => handleSelect(e.target.value)}>
                <FormControlLabel value="A" control={<Radio />} label={`A) ${currentQ.optionA}`} sx={{ mb: 1 }} />
                <FormControlLabel value="B" control={<Radio />} label={`B) ${currentQ.optionB}`} sx={{ mb: 1 }} />
                <FormControlLabel value="C" control={<Radio />} label={`C) ${currentQ.optionC}`} sx={{ mb: 1 }} />
                <FormControlLabel value="D" control={<Radio />} label={`D) ${currentQ.optionD}`} sx={{ mb: 1 }} />
              </RadioGroup>
            </FormControl>

            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
              <Button
                variant="outlined"
                startIcon={<ArrowBackIcon />}
                disabled={currentIdx === 0}
                onClick={() => setCurrentIdx(i => i - 1)}
              >
                Previous
              </Button>

              {currentIdx < questions.length - 1 ? (
                <Button
                  variant="contained"
                  endIcon={<ArrowForwardIcon />}
                  disabled={!selectedAnswers[currentQ.id]}
                  onClick={() => setCurrentIdx(i => i + 1)}
                >
                  Next
                </Button>
              ) : (
                <Button
                  variant="contained"
                  color="success"
                  disabled={answeredCount < questions.length || submitting}
                  onClick={handleSubmit}
                  sx={{ px: 3, fontWeight: 700 }}
                >
                  {submitting ? <CircularProgress size={20} color="inherit" /> : 'Submit Quiz'}
                </Button>
              )}
            </Box>
          </CardContent>
        </Card>
      )}
    </Container>
  );
};

export default QuizPage;
