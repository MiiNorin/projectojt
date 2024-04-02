package demo.controller;
import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.Questions;
import demo.repository.ChapterRepository;
import demo.repository.QuestionRepository;
import demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/showQuestion/{chapterId}")
    public String getListQuestion(Model model, @RequestParam(defaultValue = "0") int page, @PathVariable("chapterId") String chapterIdStr) {
        int chapterId;
        try {
            chapterId = Integer.parseInt(chapterIdStr);
        } catch (NumberFormatException e) {
            return "error";
        }
        int pageSize = 5;
        Page<Questions> questionPage = questionService.findProductsWithPaginationSortedByDate(page, pageSize, chapterId);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages()); // Số trang bắt đầu từ 0
        return "question";
    }


    @GetMapping("/createQuestionList/{chapterId}")
    public String showCreateQuestionPage(Model model, @PathVariable("chapterId") int chapterId) {
        model.addAttribute("questionDto", new QuestionDto());
        model.addAttribute("chapterId", chapterId);
        return "createQuestion";
    }

    @PostMapping("/createQuestionList")
    public String createQuestion(@Valid @ModelAttribute QuestionDto questionDto, @RequestParam("chapterId") int chapterId, @RequestParam("status") String status) throws IOException {

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
        questionRepository.save(questions);

        return "redirect:/questions/showQuestion/" + chapterId;
    }

    @GetMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("id") int id, @RequestParam("chapterId") int chapterId) {
        questionService.deleteQuestionById(id);
        return "redirect:/questions/showQuestion/" + chapterId;
    }

    @GetMapping("/searchByStatus")
    public String searchByStatus(Model model, @RequestParam("status") String status) {
        model.addAttribute("questions", questionRepository.findAllByStatus(status));
        return "question";
    }

    @PostMapping("/import")
    public String uploadQuestionData(@RequestParam("file") MultipartFile file) {
        questionService.saveQuestionToDatabase(file);
        return "redirect:/questions/showQuestion/1";
    }
}
