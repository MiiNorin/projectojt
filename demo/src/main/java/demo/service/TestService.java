package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> generateTest() {
        // Lấy ra ngẫu nhiên 40 câu hỏi từ cơ sở dữ liệu
        List<Questions> allQuestions = questionRepository.findAll();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, 40);
    }
}