package WebApplication.Services;

import WebApplication.Entity.QuestionsEntity;
import WebApplication.Repositories.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<QuestionsEntity> getQuestion(){
        return questionRepository.findAll();
    }
    public Optional<QuestionsEntity> getQuestionById(int id) {
        return questionRepository.findById(id);
    }
    public Page<QuestionsEntity> findProductsWithPagination(int offset, int pageSize){
        Page<QuestionsEntity> products = questionRepository.findAll(PageRequest.of(offset, pageSize));
        return  products;
    }

    public void deleteQuestionById(int id) {
        QuestionsEntity questions = questionRepository.findById(id).orElse(null);
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

    public void save(QuestionsEntity questions){
        questionRepository.save(questions);
    }
    public List<QuestionsEntity> searchQuestionByCharacter(String character) {
        return questionRepository.findByQuestionContextContaining(character);
    }

    public List<QuestionsEntity> searchQuestByDate(Date date){
        return questionRepository.findByCreateDate(date);
    }
    public void saveQuestionToDatabase(MultipartFile file){
        if(ExcelUploadService.isValidExcelFile(file)){
            try {
                List<QuestionsEntity> questionsList = ExcelUploadService.getQuestionDataFromExcel(file.getInputStream());
                this.questionRepository.saveAll(questionsList);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

}