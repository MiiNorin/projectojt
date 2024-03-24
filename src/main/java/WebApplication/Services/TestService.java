package WebApplication.Services;


import WebApplication.Entity.QuestionsEntity;
import WebApplication.Repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionsEntity> generateTest() {
        // Lấy ra ngẫu nhiên 40 câu hỏi từ cơ sở dữ liệu
        List<QuestionsEntity> allQuestions = questionRepository.findAll();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, 40);
    }
}


