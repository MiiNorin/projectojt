package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@Service
@AllArgsConstructor
public class QuestionService{
    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> getQuestion(){
        return questionRepository.findAll();
    }
    public Optional<Questions> getQuestionById(int id) {
        return questionRepository.findById(id);
    }


    public void deleteQuestionById(int id) {
        Questions questions = questionRepository.findById(id).orElse(null);
        if (questions != null) {

            if (questions.getImage() != null) {
                String imagePath = "public/images/" + questions.getImage();
                try {
                    Path path = Paths.get(imagePath);
                    Files.deleteIfExists(path);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            questionRepository.deleteById(id);
        }
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
    public void saveQuestionToDatabase(MultipartFile file){
        if(ExcelUploadService.isValidExcelFile(file)){
            try {
                List<Questions> questionsList = ExcelUploadService.getQuestionDataFromExcel(file.getInputStream());
                this.questionRepository.saveAll(questionsList);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

}
