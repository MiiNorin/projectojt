package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ejb.Local;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@AllArgsConstructor
public class QuestionService{
    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> getQuestion(){
        return questionRepository.findAll();
    }


    public Page<Questions> findProductsWithPaginationSortedByDate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return questionRepository.findAll(pageable);
    }

//    public Optional<Questions> getQuestionById(int id) {
//        return questionRepository.findById(id);
//    }
//    public Page<Questions> findProductsWithPagination(int offset,int pageSize){
//        Page<Questions> products = questionRepository.findAll(PageRequest.of(offset, pageSize));
//        return  products;
//    }

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

    public List<Questions> searchQuestByMonth(int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(LocalDate.now().getYear(), month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        return questionRepository.findByCreateDateBetween(startOfMonth, endOfMonth);
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
