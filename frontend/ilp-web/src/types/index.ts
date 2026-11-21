export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface User {
  id?: number;
  username: string;
  streak: number;
  dailyGoalMinutes: number;
  progressPercentage: number;
  lastLoginDate?: string;
}

export interface Course {
  id: number;
  name: string;
  description: string;
  totalLessons: number;
}

export interface Lesson {
  id: number;
  courseId: number;
  title: string;
  content: string;
  difficultyLevel: number;
  difficultyDescription?: string;
}

export interface QuizQuestion {
  id: number;
  lessonId: number;
  question: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
}

export interface QuizResult {
  score: number;
  totalQuestions: number;
  quizPercentage: number;
  newOverallProgressPercentage: number;
  passed: boolean;
  feedbackMessage: string;
}

export interface CourseProgress {
  courseId: number;
  totalLessons: number;
  completedLessons: number;
  progressPercentage: number;
  canTakeQuiz: boolean;
  completedLessonIds: number[];
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}
