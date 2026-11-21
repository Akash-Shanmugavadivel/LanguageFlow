package com.learning.languageflow.service;

import com.learning.languageflow.dao.LessonDao;
import com.learning.languageflow.dao.LessonProgressDao;
import com.learning.languageflow.dto.CourseProgressResponse;
import com.learning.languageflow.model.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private final LessonDao lessonDao;
    private final LessonProgressDao lessonProgressDao;
    private final ProgressService progressService;

    public LessonService(LessonDao lessonDao, LessonProgressDao lessonProgressDao, ProgressService progressService) {
        this.lessonDao = lessonDao;
        this.lessonProgressDao = lessonProgressDao;
        this.progressService = progressService;
    }

    public List<Lesson> getLessonsByCourse(int courseId) {
        return lessonDao.findByCourseId(courseId);
    }

    public Lesson getLessonById(int lessonId) {
        return lessonDao.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
    }

    public Optional<Lesson> getLessonByOffset(int courseId, int offset) {
        return lessonDao.findByCourseIdAndOffset(courseId, offset);
    }

    public int getTotalLessonsCount(int courseId) {
        return lessonDao.countByCourseId(courseId);
    }

    public int getCompletedLessonsCount(String username, int courseId) {
        return lessonProgressDao.countCompletedByCourseId(username, courseId);
    }

    public List<Integer> getCompletedLessonIds(String username, int courseId) {
        return lessonProgressDao.findCompletedLessonIdsByCourseId(username, courseId);
    }

    public double completeLesson(String username, int lessonId, int courseId) {
        lessonProgressDao.markLessonCompleted(username, lessonId);

        int totalLessons = lessonDao.countByCourseId(courseId);
        int completedLessons = lessonProgressDao.countCompletedByCourseId(username, courseId);

        double progressPercentage = 0.0;
        if (totalLessons > 0) {
            progressPercentage = (completedLessons / (double) totalLessons) * 100.0;
        }

        progressService.updateProgressPercentage(username, progressPercentage);
        return progressPercentage;
    }

    public boolean canTakeQuiz(String username, int courseId) {
        int totalLessons = lessonDao.countByCourseId(courseId);
        if (totalLessons == 0) {
            return false;
        }
        int completedLessons = lessonProgressDao.countCompletedByCourseId(username, courseId);
        return completedLessons >= totalLessons;
    }

    public CourseProgressResponse getCourseProgress(String username, int courseId) {
        int total = getTotalLessonsCount(courseId);
        int completed = getCompletedLessonsCount(username, courseId);
        double percentage = total > 0 ? (completed / (double) total) * 100.0 : 0.0;
        boolean canQuiz = total > 0 && completed >= total;
        List<Integer> completedIds = getCompletedLessonIds(username, courseId);

        return new CourseProgressResponse(courseId, total, completed, percentage, canQuiz, completedIds);
    }

    public Lesson createLesson(Lesson lesson) {
        if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Lesson title cannot be empty.");
        }
        return lessonDao.save(lesson);
    }

    public Lesson updateLesson(int id, Lesson updatedLesson) {
        Lesson existing = getLessonById(id);
        if (updatedLesson.getTitle() != null) existing.setTitle(updatedLesson.getTitle());
        if (updatedLesson.getContent() != null) existing.setContent(updatedLesson.getContent());
        if (updatedLesson.getCourseId() != null) existing.setCourseId(updatedLesson.getCourseId());
        if (updatedLesson.getDifficultyLevel() != null) existing.setDifficultyLevel(updatedLesson.getDifficultyLevel());
        return lessonDao.save(existing);
    }

    public boolean deleteLesson(int id) {
        return lessonDao.deleteById(id);
    }
}
