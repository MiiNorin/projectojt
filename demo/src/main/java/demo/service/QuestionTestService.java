package demo.service;

import demo.persistence.entity.Questions;
import demo.persistence.entity.QuestiontestsEntity;
import demo.repository.QuestionRepository;
import demo.repository.QuestionTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTestService {

    @Autowired
    private QuestionTestRepository questionTestRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public void saveAnswer(Integer questionId, Integer selectedAnswer, Integer testDetailId) {
        QuestiontestsEntity q = new QuestiontestsEntity();
        q.setQuestionId(questionId);
        q.setAnswerId(selectedAnswer);
        q.setTestDetailId(testDetailId);
        questionTestRepository.save(q);
    }

    public List<QuestiontestsEntity> getQuestionTests() {
        // Lấy danh sách các question_id và answer_id từ cơ sở dữ liệu
        return questionTestRepository.findAll();
    }

    public List<Questions> getAllQuestions() {
        return questionRepository.findAll();
    }

    public void saveAllQuestionTests(List<QuestiontestsEntity> questionTestsList) {
    }

    public List<QuestiontestsEntity> getQuestionTestsByTestDetailId(int testDetailId) {

        return questionTestRepository.findByTestDetailId(testDetailId);

    }
}