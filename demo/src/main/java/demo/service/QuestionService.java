package demo.service;

import demo.entity.Questions;
import demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService{
    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> getQuestion(){
        return questionRepository.findAll();
    }


    public void deleteQuestionById(int id) {

        questionRepository.deleteById(id);
    }
    public void save(Questions questions){
        questionRepository.save(questions);
    }
    public List<Questions> searchQuestionByCharacter(String character) {
        return questionRepository.findByQuestionContextContaining(character);
    }

    public List<Questions> searchQuestByDate(Date date){
        return questionRepository.findByCreateDate(date);
    }

}
