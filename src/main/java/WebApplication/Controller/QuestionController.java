package WebApplication.Controller;


import WebApplication.Entity.Dto.QuestionDto;
import WebApplication.Entity.QuestionsEntity;
import WebApplication.Repositories.QuestionRepository;
import WebApplication.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Page<QuestionsEntity> questionPage = questionService.findProductsWithPagination(page, pageSize);
        List<QuestionsEntity> questions = questionPage.getContent();
        model.addAttribute("questions", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages() - 1); // Số trang bắt đầu từ 0
        return "question";
    }

    @GetMapping("/getQuestionDetails")
    public String getQuestionDetails(Model model, @RequestParam("questionId") int questionId) {
        try {
            QuestionsEntity question = questionRepository.findById(questionId).orElse(null);
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
            @ModelAttribute QuestionDto questionDto,
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

        QuestionsEntity questions = new QuestionsEntity();
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
            QuestionsEntity questions = questionRepository.findById(questionId).get();
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
    public String editQuestion(Model model, @RequestParam("id") int id, @ModelAttribute QuestionDto questionDto, BindingResult result) {
        try {
            QuestionsEntity questions = questionRepository.findById(id).get();
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

    // Phương thức xóa ảnh
    @GetMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("id") int id) {
        try {
            QuestionsEntity question = questionRepository.findById(id).orElse(null);
            if (question != null) {
                deleteImage(question.getImage());
                questionRepository.delete(question);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/questions/showQuestion";
    }

    @GetMapping("/showQuestionById")
    public String showQuestionById(Model model, @RequestParam("id") int questionId) {
        try {
            QuestionsEntity questions = questionRepository.findById(questionId).orElse(null);
            if (questions != null) {
                model.addAttribute("question", questions);
                return "questionForCreateTest";
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/questions/showQuestion";
    }

    @GetMapping("/searchQuestion")
    public String searchQuestionByCharacter(Model model, @RequestParam("character") String character) {
        try {
            List<QuestionsEntity> questions = questionService.searchQuestionByCharacter(character);
            model.addAttribute("questions", questions);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "question";
    }

    @GetMapping("/searchByStatus")
    public String searchByStatus(Model model, @RequestParam("status") String status) {
        try {
            List<QuestionsEntity> questions;
            if (status.equals("hard") || status.equals("easy")) {
                questions = questionRepository.findAllByStatus(status);
            } else {
                questions = questionService.getQuestion();
            }
            model.addAttribute("questions", questions);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "question";
    }

    @PostMapping("/import")
    public String uploadQuestionData(@RequestParam("file") MultipartFile file){
        try {
            this.questionService.saveQuestionToDatabase(file);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/questions/showQuestion";
    }



    private void deleteImage(String storageFile) {
        if (storageFile != null && !storageFile.isEmpty()) {
            try {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + storageFile);
                Files.delete(oldImagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        }
    }


}