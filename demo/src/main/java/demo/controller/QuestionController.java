package demo.controller;
import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import demo.service.ExcelUploadService;
import demo.service.QuestionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import java.io.File;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.*;
import java.util.Date;
import java.util.List;


@RequestMapping("/questions")
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResourceLoader resourceLoader;
    @GetMapping("/showQuestion")
    public String getListQuestion(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 4; // Số lượng câu hỏi trên mỗi trang
        Page<Questions> questionPage = questionService.findProductsWithPagination(page, pageSize);
        List<Questions> questions = questionPage.getContent();
        model.addAttribute("questions", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages() - 1); // Số trang bắt đầu từ 0
        return "question";
    }


    @GetMapping("/getQuestionDetails")
    public String getQuestionDetails(Model model, @RequestParam("questionId") int questionId) {
        try {
            Questions question = questionRepository.findById(questionId).orElse(null);
            if (question == null) {
                return "redirect:/questions/showQuestion";
            }
            model.addAttribute("question", question);
            return "createTestPage";
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/questions/showQuestion";
        }
    }


    @GetMapping("/createQuestionList")
    public String showCreateQuestionPage(Model model) {
        QuestionDto questionDto = new QuestionDto();
        model.addAttribute("questionDto", questionDto);
        return "createQuestion";
    }

    @PostMapping("/createQuestionList")
    public String createQuestion(
            @Valid @ModelAttribute QuestionDto questionDto,
            BindingResult result) {
//        if(questionDto.getQuestionContext().isEmpty()) {
//            result.addError(new FieldError("questionDto", "questionContext", "The question context is required"));
//        }
        if (result.hasErrors()) {
//            result.addError(new FieldError());
            return "createQuestion";
        }
        MultipartFile image = questionDto.getImage();
        String storageFile = "";

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Date createDate = new Date();
                storageFile = createDate.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFile),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
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
        questions.setStatus(questionDto.getStatus());
        questionRepository.save(questions);

        return "redirect:/questions/showQuestion";
    }

    @GetMapping("/editQuestion")
    public String showEditPage(Model model, @RequestParam("id") int questionId) {
        try {
            Questions questions = questionRepository.findById(questionId).get();
            model.addAttribute("question", questions);
            QuestionDto questionDto = new QuestionDto();
            questionDto.setQuestionContext(questions.getQuestionContext());
            questionDto.setOptionA(questions.getOptionA());
            questionDto.setOptionB(questions.getOptionB());
            questionDto.setOptionC(questions.getOptionC());
            questionDto.setOptionD(questions.getOptionD());
            questionDto.setStatus(questions.getStatus());
            questionDto.setSolution(questions.getSolution());
            model.addAttribute("questionDto", questionDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/questions";
        }
        return "/editQuestion";
    }

    @PostMapping("/editQuestion")
    public String editQuestion(Model model, @RequestParam("id") int id, @Valid @ModelAttribute QuestionDto questionDto, BindingResult result) {
        try {
            Questions questions = questionRepository.findById(id).get();
            model.addAttribute(questions);
            if (result.hasErrors()) {
                return "/editQuestion";
            }
            if (!questionDto.getImage().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + questions.getImage());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                MultipartFile image = questionDto.getImage();
                Date createDate = new Date();
                String storageFile = createDate.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFile),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                questions.setImage(storageFile);
            }
            questions.setQuestionContext(questionDto.getQuestionContext());
            questions.setOptionA(questionDto.getOptionA());
            questions.setOptionB(questionDto.getOptionB());
            questions.setOptionC(questionDto.getOptionC());
            questions.setOptionD(questionDto.getOptionD());
            questions.setSolution(questionDto.getSolution());
            questions.setStatus(questionDto.getStatus());
            questionRepository.save(questions);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/questions/showQuestion";
    }


    @GetMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("id") int id) {
        questionService.deleteQuestionById(id);
        return "redirect:/questions/showQuestion";
    }

    @GetMapping("/showQuestionById")
    public String showQuestionById(Model model, @RequestParam("id") int questionId) {
        Questions questions = questionRepository.findById(questionId).get();
        model.addAttribute("question", questions);
        return "questionForCreateTest";
    }

    @GetMapping("/searchQuestion")
    public String searchQuestionByCharacter(Model model, @RequestParam("character") String character) {
        List<Questions> questions = questionService.searchQuestionByCharacter(character);
        model.addAttribute("questions", questions);
        return "question";
    }

    @GetMapping("/searchByStatus")
    public String searchByStatus(Model model, @RequestParam("status") String status) {
        List<Questions> questions;
        if (status.equals("hard") || status.equals("easy")) {
            questions = questionRepository.findAllByStatus(status);
        } else {
            questions = questionService.getQuestion();
        }
        model.addAttribute("questions", questions);
        return "question";
    }

    @PostMapping("/import")
    public String uploadQuestionData(@RequestParam("file") MultipartFile file){
        this.questionService.saveQuestionToDatabase(file);
        return "redirect:/questions/showQuestion";
    }


}