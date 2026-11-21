import React, { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import apiClient from '../api/client';
import { useAuth } from './AuthContext';
import { CourseProgress, ApiResponse } from '../types';

interface ProgressContextType {
  streak: number;
  dailyGoalMinutes: number;
  overallProgress: number;
  courseProgressMap: Record<number, CourseProgress>;
  isLoadingProgress: boolean;
  updateDailyGoal: (minutes: number) => Promise<boolean>;
  completeLesson: (lessonId: number, courseId: number) => Promise<boolean>;
  fetchCourseProgress: (courseId: number) => Promise<CourseProgress | null>;
  incrementStreak: () => Promise<void>;
  resetStreak: () => Promise<void>;
  refreshAllProgress: () => Promise<void>;
}

const ProgressContext = createContext<ProgressContextType | undefined>(undefined);

export const ProgressProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const { user, refreshUser } = useAuth();
  const [courseProgressMap, setCourseProgressMap] = useState<Record<number, CourseProgress>>({});
  const [isLoadingProgress, setIsLoadingProgress] = useState<boolean>(false);

  const streak = user?.streak || 0;
  const dailyGoalMinutes = user?.dailyGoalMinutes || 15;
  const overallProgress = user?.progressPercentage || 0.0;

  const fetchCourseProgress = useCallback(
    async (courseId: number): Promise<CourseProgress | null> => {
      if (!user?.username) return null;
      try {
        const response = await apiClient.get<ApiResponse<CourseProgress>>(
          `/lessons/course/${courseId}/progress`,
          { params: { username: user.username } }
        );
        if (response.data && response.data.data) {
          const progress = response.data.data;
          setCourseProgressMap((prev) => ({
            ...prev,
            [courseId]: progress,
          }));
          return progress;
        }
      } catch (err) {
        console.error(`Failed to fetch course ${courseId} progress:`, err);
      }
      return null;
    },
    [user?.username]
  );

  const updateDailyGoal = async (minutes: number): Promise<boolean> => {
    if (!user?.username || minutes <= 0) return false;
    try {
      const response = await apiClient.put<ApiResponse<void>>(`/users/${user.username}/daily-goal`, {
        dailyGoalMinutes: minutes,
      });
      if (response.data.success) {
        await refreshUser();
        return true;
      }
    } catch (err) {
      console.error('Failed to update daily goal:', err);
    }
    return false;
  };

  const completeLesson = async (lessonId: number, courseId: number): Promise<boolean> => {
    if (!user?.username) return false;
    try {
      const response = await apiClient.post<ApiResponse<number>>(`/lessons/${lessonId}/complete`, {
        username: user.username,
        courseId,
      });

      if (response.data.success) {
        await fetchCourseProgress(courseId);
        await refreshUser();
        return true;
      }
    } catch (err) {
      console.error('Failed to mark lesson completed:', err);
    }
    return false;
  };

  const incrementStreak = async () => {
    if (!user?.username) return;
    try {
      await apiClient.post(`/users/${user.username}/streak/increment`);
      await refreshUser();
    } catch (err) {
      console.error('Failed to increment streak:', err);
    }
  };

  const resetStreak = async () => {
    if (!user?.username) return;
    try {
      await apiClient.post(`/users/${user.username}/streak/reset`);
      await refreshUser();
    } catch (err) {
      console.error('Failed to reset streak:', err);
    }
  };

  const refreshAllProgress = async () => {
    await refreshUser();
  };

  return (
    <ProgressContext.Provider
      value={{
        streak,
        dailyGoalMinutes,
        overallProgress,
        courseProgressMap,
        isLoadingProgress,
        updateDailyGoal,
        completeLesson,
        fetchCourseProgress,
        incrementStreak,
        resetStreak,
        refreshAllProgress,
      }}
    >
      {children}
    </ProgressContext.Provider>
  );
};

export const useProgress = (): ProgressContextType => {
  const context = useContext(ProgressContext);
  if (!context) {
    throw new Error('useProgress must be used within a ProgressProvider');
  }
  return context;
};
