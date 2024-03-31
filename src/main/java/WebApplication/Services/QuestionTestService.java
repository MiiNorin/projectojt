package WebApplication.Services;

import WebApplication.Entity.QuestiontestsEntity;
import WebApplication.Repositories.QuestionTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionTestService {

    @Autowired
    private QuestionTestRepository questionTestRepository;

    public void saveAnswer(Integer questionId, Integer selectedAnswer) {
        QuestiontestsEntity q = new QuestiontestsEntity();
        q.setQuestionId(questionId);
        q.setAnswerId(selectedAnswer);
        // Lưu câu trả lời vào cơ sở dữ liệu
        questionTestRepository.save(q);
    }
}
