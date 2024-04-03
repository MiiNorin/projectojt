package demo.service;

import demo.persistence.entity.Questions;
import demo.repository.ChapterRepository;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import demo.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService{
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    public List<Questions> getQuestion(){
        return questionRepository.findAll();
    }
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    public Page<Questions> findProductsWithPaginationSortedByDate(int page, int size, int chapterId, int subjectId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return questionRepository.findByChaptersChapterId(chapterId, pageable);
    }

    public Page<Questions> findQuestionsWithPaginationSortByDate(int page, int size, int subjectId){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return questionRepository.findBySubjectSubjectId(subjectId, pageable);
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

    public List<Questions> findQuestionsByName(String name) {
        return questionRepository.findByQuestionContextContaining(name);
    }


    public void saveQuestionToDatabase(MultipartFile file, int subjectId, int chapterId, int topicId) {
        if (ExcelUploadService.isValidExcelFile(file)) {
            try {
                List<Questions> questionsList = ExcelUploadService.getQuestionDataFromExcel(file.getInputStream());
                for (Questions question : questionsList) {
                    question.setChapters(chapterRepository.findById(chapterId).orElse(null));
                    question.setSubject(subjectRepository.findById(subjectId).orElse(null));
                    question.setTopics(topicRepository.findById(topicId).orElse(null));
                }
                this.questionRepository.saveAll(questionsList);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

//    public Page<Questions> searchQuestionsByName(Pageable pageable, String name, int chapterId) {
//        return questionRepository.findByQuestionContextContaining(name, pageable);
//    }
}
