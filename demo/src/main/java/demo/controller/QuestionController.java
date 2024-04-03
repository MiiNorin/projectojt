package demo.controller;

import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.Questions;
import demo.repository.ChapterRepository;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import demo.repository.TopicRepository;
import demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TopicRepository topicRepository;
    @GetMapping("/showQuestion/{chapterId}")
    public String getListQuestion(Model model, @RequestParam(defaultValue = "0") int page, @PathVariable("chapterId") int chapterId) {
        int pageSize = 5;
        Page<Questions> questionPage = questionService.findProductsWithPaginationSortedByDate(page, pageSize, chapterId);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        return "listQuestionByChapter";
    }

    @GetMapping("subject/{subjectId}")
    public String getListQuestionBySubject(Model model, @RequestParam(defaultValue = "0") int page, @PathVariable("subjectId") int subjectId){
        int pageSize = 5;
        Page<Questions> questionPage = questionService.findQuestionsWithPaginationSortByDate(page, pageSize, subjectId);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("subjectId", subjectId);
        return "listQuestionBySubject";
    }

    @GetMapping("/createQuestionList/{subjectId}/{chapterId}/{topicId}")
    public String showCreateQuestionPage(Model model, @PathVariable("subjectId") int subjectId, @PathVariable("chapterId") int chapterId, @PathVariable("topicId") int topicId) {
        model.addAttribute("questionDto", new QuestionDto());
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("topicId", topicId);
        return "createQuestion";
    }
    @PostMapping("/createQuestionList")
    public String createQuestion(@Valid @ModelAttribute QuestionDto questionDto,
                                 @RequestParam("chapterId") Integer chapterId,
                                 @RequestParam("subjectId") Integer subjectId,
                                 @RequestParam("topicId") Integer topicId,
                                 @RequestParam("status") String status) throws IOException {

        MultipartFile image = questionDto.getImage();
        String storageFile = "";

        if (image != null && !image.isEmpty()) {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Date createDate = new Date();
            storageFile = createDate.getTime() + "_" + image.getOriginalFilename();
            try {
                Files.copy(image.getInputStream(), uploadPath.resolve(storageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Questions questions = new Questions();
        questions.setQuestionContext(questionDto.getQuestionContext());
        questions.setOptionA(questionDto.getOptionA());
        questions.setOptionB(questionDto.getOptionB());
        questions.setOptionC(questionDto.getOptionC());
        questions.setOptionD(questionDto.getOptionD());
        questions.setSolution(questionDto.getSolution());
        questions.setImage(storageFile);
        questions.setStatus(status);
        questions.setCreateDate(LocalDateTime.now());
        questions.setChapters(chapterRepository.findById(chapterId).orElse(null));
        questions.setSubject(subjectRepository.findById(subjectId).orElse(null));
        questions.setTopics(topicRepository.findById(topicId).orElse(null));
        questionRepository.save(questions);

        return "redirect:/questions/showQuestion/" + chapterId;
    }
    @GetMapping("/deleteQuestionInChapter")
    public String deleteQuestionInChapter(@RequestParam("id") int id, @RequestParam("chapterId") int chapterId) {
        questionService.deleteQuestionById(id);
        return "redirect:/questions/showQuestion/" + chapterId;
    }
    @GetMapping("/deleteQuestionInSubject")
    public String deleteQuestionInSubject(@RequestParam("id") int id, @RequestParam("subjectId") int subjectId) {
        questionService.deleteQuestionById(id);
        return "redirect:/questions/subject/" + subjectId;
    }

    @GetMapping("/searchByStatus")
    public String searchByStatus(Model model, @RequestParam("status") String status) {
        model.addAttribute("questions", questionRepository.findAllByStatus(status));
        return "listQuestionByChapter";
    }


    @PostMapping("/import/{subjectId}/{chapterId}/{topicId}")
    public String uploadQuestionData(@PathVariable("subjectId") int subjectId,
                                     @PathVariable("chapterId") int chapterId,
                                     @PathVariable("topicId") int topicId,
                                     @RequestParam("file") MultipartFile file) {
        questionService.saveQuestionToDatabase(file, subjectId, chapterId, topicId);
        return "redirect:/questions/showQuestion/" + chapterId;
    }

    @GetMapping("/editQuestion")
    public String showEditQuestionForm(Model model, @RequestParam("id") int questionId, @RequestParam("chapterId") int chapterId) {
        try {
            Questions question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                model.addAttribute("question", question);
                model.addAttribute("questionId", questionId);
                model.addAttribute("chapterId", chapterId);
                return "editQuestion";
            } else {
                return "redirect:/questions/showQuestion/" + chapterId;
            }
        } catch (Exception ex) {
            // Xử lý ngoại lệ
            return "redirect:/questions/showQuestion/" + chapterId;
        }
    }

    @PostMapping("/editQuestion")
    public String editQuestion(@RequestParam("id") int questionId,
                               @RequestParam("chapterId") int chapterId,
                               @ModelAttribute QuestionDto questionDto) {
        try {
            Questions question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                // Cập nhật thông tin câu hỏi
                question.setQuestionContext(questionDto.getQuestionContext());
                question.setOptionA(questionDto.getOptionA());
                question.setOptionB(questionDto.getOptionB());
                question.setOptionC(questionDto.getOptionC());
                question.setOptionD(questionDto.getOptionD());
                question.setSolution(questionDto.getSolution());
                question.setStatus(questionDto.getStatus());
                questionRepository.save(question);
            }
            return "redirect:/questions/showQuestion/" + chapterId;
        } catch (Exception ex) {
            // Xử lý ngoại lệ
            return "redirect:/questions/showQuestion/" + chapterId;
        }
    }

}
