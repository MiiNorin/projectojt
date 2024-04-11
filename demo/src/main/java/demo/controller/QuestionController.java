package demo.controller;

import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.persistence.entity.TopicsEntity;
import demo.repository.*;
import demo.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import java.util.List;

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

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/showQuestion/{subjectId}/{chapterId}")
    public String getListQuestion(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @PathVariable("subjectId") int subjectId,
                                  @PathVariable("chapterId") int chapterId,
                                  HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId)) {
            return "redirect:/home/homePage";
        }
        int totalQuestion = questionRepository.countByChaptersChapterId(chapterId);
        ChaptersEntity chapters = chapterRepository.findById(chapterId).orElse(null);
        String chapterName = chapters.getChapterName();
        chapters.setTotalQuestion(totalQuestion);
        chapterRepository.save(chapters);
        int pageSize = 5;
        Page<Questions> questionPage = questionService.findProductsWithPaginationSortedByDate(page, pageSize, chapterId, subjectId);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("totalQuestion", totalQuestion);
        model.addAttribute("chapterName", chapterName);
        return "listQuestionByChapter";
    }


    @GetMapping("subject/{subjectId}")
    public String getListQuestionBySubject(Model model, @RequestParam(defaultValue = "0") int page, @PathVariable("subjectId") int subjectId) {
        int pageSize = 5;
        Page<Questions> questionPage = questionService.findQuestionsWithPaginationSortByDate(page, pageSize, subjectId);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("subjectId", subjectId);
        return "listQuestionBySubject";
    }

    @GetMapping("/createQuestionList/{subjectId}/{chapterId}/{topicId}")
    public String showCreateQuestionPage(Model model,
                                         @PathVariable("subjectId") int subjectId,
                                         @PathVariable("chapterId") int chapterId,
                                         @PathVariable("topicId") int topicId,
                                         HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId || userId != createdTopicUserId)) {
            return "/home/homePage";
        }
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
                                 @RequestParam("status") String status,
//                                 @RequestParam("answerId") Integer answerId,
//                                 Model model,
                                 HttpSession session) throws IOException {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId || userId != createdTopicUserId)) {
            return "/home/homePage";
        }
        ChaptersEntity chapter = chapterRepository.findById(chapterId).orElse(null);
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
        questions.setAnswerId(questionDto.getAnswerId());
        questions.setImage(storageFile);
        questions.setStatus(status);
        questions.setCreateDate(LocalDateTime.now());
        questions.setChapters(chapter);
        questions.setSubject(subjectRepository.findById(subjectId).orElse(null));
        questions.setTopics(topicRepository.findById(topicId).orElse(null));
        questionRepository.save(questions);
        System.out.println(questions);
        return "redirect:/questions/showQuestion/" + subjectId + '/' + chapterId;
    }

    @GetMapping("/editQuestion")
    public String showEditQuestionForm(Model model,
                                       @RequestParam("id") int questionId,
                                       @RequestParam("subjectId") int subjectId,
                                       @RequestParam("chapterId") int chapterId,
                                       HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        try {
            Questions question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                model.addAttribute("question", question);
                model.addAttribute("questionId", questionId);
                model.addAttribute("chapterId", chapterId);
                model.addAttribute("subjectId", subjectId);
                return "editQuestion";
            } else {
                return "redirect:/questions/showQuestion/" + chapterId;
            }
        } catch (Exception ex) {
            return "redirect:/questions/showQuestion/" + chapterId;
        }
    }

    @PostMapping("/editQuestion")
    public String editQuestion(@RequestParam("id") int questionId,
                               @RequestParam("chapterId") int chapterId,
                               @RequestParam("subjectId") int subjectId,
                               @RequestParam(name = "deleteImage", required = false) boolean deleteImage,
                               @RequestParam(name = "newImage", required = false) MultipartFile newImage,
                               @ModelAttribute QuestionDto questionDto,
                               HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        try {
            Questions question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                if (deleteImage) {
                    // Xóa ảnh từ thư mục lưu trữ (nếu cần)
                    if (question.getImage() != null && !question.getImage().isEmpty()) {
                        String imagePath = "public/images/" + question.getImage(); // Đường dẫn đến ảnh trong thư mục lưu trữ
                        Path path = Paths.get(imagePath);
                        Files.deleteIfExists(path);

                        // Cập nhật lại đường dẫn ảnh trong cơ sở dữ liệu
                        question.setImage(null);
                    }
                }

                // Xử lý ảnh mới nếu được tải lên
                if (newImage != null && !newImage.isEmpty()) {
                    String fileName = StringUtils.cleanPath(newImage.getOriginalFilename());
                    String uploadDir = "public/images/";
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    try (InputStream inputStream = newImage.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        question.setImage(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not save the file: " + e.getMessage());
                    }
                }

                // Cập nhật thông tin câu hỏi
                question.setQuestionContext(questionDto.getQuestionContext());
                question.setOptionA(questionDto.getOptionA());
                question.setOptionB(questionDto.getOptionB());
                question.setOptionC(questionDto.getOptionC());
                question.setOptionD(questionDto.getOptionD());
                question.setSolution(questionDto.getSolution());
                question.setAnswerId(questionDto.getAnswerId());
                question.setStatus(questionDto.getStatus());
                questionRepository.save(question);
            }
            return "redirect:/questions/showQuestion/" + subjectId + '/' + chapterId;
        } catch (Exception ex) {
            // Xử lý ngoại lệ
            return "redirect:/questions/showQuestion/" + subjectId + '/' + chapterId;
        }
    }


    @GetMapping("/deleteQuestionInChapter")
    public String deleteQuestionInChapter(@RequestParam("id") int id,
                                          @RequestParam("subjectId") int subjectId,
                                          @RequestParam("chapterId") int chapterId,
                                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        questionService.deleteQuestionById(id);
        return "redirect:/questions/showQuestion/" + subjectId + '/' + chapterId;
    }

    @GetMapping("/deleteQuestionInSubject")
    public String deleteQuestionInSubject(@RequestParam("id") int id,
                                          @RequestParam("subjectId") int subjectId,
                                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if (userId != null && (userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        questionService.deleteQuestionById(id);
        return "redirect:/questions/subject/" + subjectId;
    }


    @PostMapping("/import/{subjectId}/{chapterId}/{topicId}")
    public String uploadQuestionData(@PathVariable("subjectId") int subjectId,
                                     @PathVariable("chapterId") int chapterId,
                                     @PathVariable("topicId") int topicId,
                                     @RequestParam("file") MultipartFile file,
                                     HttpSession session,
                                     Model model) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId || userId != createdTopicUserId)) {
            return "/home/homePage";
        }
        try{
            questionService.saveQuestionToDatabase(file, subjectId, chapterId, topicId);
        }
        catch (Exception e) {
            model.addAttribute("error", "File không đúng định dạng");
            model.addAttribute("subjectId", subjectId);
            model.addAttribute("chapterId", chapterId);
            model.addAttribute("topicId", topicId);
            model.addAttribute("questionDto", new QuestionDto());
            return "createQuestion";
        }
        return "redirect:/questions/showQuestion/" + subjectId + '/' + chapterId;
    }


    @GetMapping("/searchByName")
    public String searchQuestionsByName(Model model,
                                        @RequestParam("searchName") String searchName,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam("chapterId") int chapterId,
                                        @RequestParam("subjectId") int subjectId,
                                        HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if (userId != null && (userId != createdChapterUserId || userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize); // Adjusted page parameter
        Page<Questions> questionPage = questionRepository.findByQuestionContextContainingAndSubjectSubjectIdAndChaptersChapterId(searchName, subjectId, chapterId, pageable);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("searchName", searchName);
        return "searchQuestionByChapterResult";
    }

    @GetMapping("/searchByNameInSubject")
    public String searchQuestionsByNameInSubject(Model model,
                                                 @RequestParam("searchName") String searchName,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam("subjectId") int subjectId,
                                                 HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if (userId != null && (userId != createdSubjectUserId)) {
            return "/home/homePage";
        }
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize); // Adjusted page parameter
        Page<Questions> questionPage = questionRepository.findByQuestionContextContainingAndSubjectSubjectId(searchName, subjectId, pageable);
        model.addAttribute("questions", questionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("searchName", searchName);
        return "searchQuestionBySubjectResult";
    }
}
