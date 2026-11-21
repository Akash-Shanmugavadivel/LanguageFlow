package com.learning.languageflow.service;

import com.learning.languageflow.dao.QuizDao;
import com.learning.languageflow.dao.UserDao;
import com.learning.languageflow.model.QuizQuestion;
import com.learning.languageflow.model.QuizResult;
import com.learning.languageflow.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Domain Service for Quiz evaluation and score aggregation.
 */
@Service
public class QuizService {

    private final QuizDao quizDao;
    private final UserDao userDao;

    public QuizService(QuizDao quizDao, UserDao userDao) {
        this.quizDao = quizDao;
        this.userDao = userDao;
    }

    public List<QuizQuestion> getQuestionsForCourse(int courseId) {
        return quizDao.findByCourseId(courseId);
    }

    public List<QuizQuestion> getQuestionsForLesson(int lessonId) {
        return quizDao.findByLessonId(lessonId);
    }

    public QuizQuestion getQuestionById(int id) {
        return quizDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz question not found with id: " + id));
    }

    public QuizResult evaluateQuiz(String username, int courseId, Map<Integer, Character> answers) {
        List<QuizQuestion> questions = quizDao.findByCourseId(courseId);
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("No quiz questions found for course id: " + courseId);
        }

        int score = 0;
        for (QuizQuestion question : questions) {
            Character selected = answers != null ? answers.get(question.getId()) : null;
            if (question.isCorrectAnswer(selected)) {
                score++;
            }
        }

        double quizPercentage = ((double) score / questions.size()) * 100.0;

        User user = userDao.findByUsername(username).orElse(null);
        double newProgress = quizPercentage;
        if (user != null) {
            user.addProgress(quizPercentage);
            newProgress = user.getProgressPercentage();
            userDao.updateProgressPercentage(username, newProgress);
        }

        return new QuizResult(score, questions.size(), quizPercentage, newProgress);
    }

    public QuizQuestion createQuestion(QuizQuestion question) {
        if (question.getQuestion() == null || question.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        return quizDao.save(question);
    }

    public QuizQuestion updateQuestion(int id, QuizQuestion updated) {
        QuizQuestion existing = getQuestionById(id);
        if (updated.getQuestion() != null) existing.setQuestion(updated.getQuestion());
        if (updated.getOptionA() != null) existing.setOptionA(updated.getOptionA());
        if (updated.getOptionB() != null) existing.setOptionB(updated.getOptionB());
        if (updated.getOptionC() != null) existing.setOptionC(updated.getOptionC());
        if (updated.getOptionD() != null) existing.setOptionD(updated.getOptionD());
        if (updated.getCorrectOption() != ' ') existing.setCorrectOption(updated.getCorrectOption());
        if (updated.getLessonId() != null) existing.setLessonId(updated.getLessonId());
        return quizDao.save(existing);
    }

    public boolean deleteQuestion(int id) {
        return quizDao.deleteById(id);
    }
}
