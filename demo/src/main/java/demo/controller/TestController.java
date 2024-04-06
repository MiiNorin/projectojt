package demo.controller;


import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.persistence.entity.TopicsEntity;
import demo.repository.AccountRepository;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import demo.repository.TopicRepository;
import demo.service.QuestionService;
import demo.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RequestMapping("/test")
@Controller
public class TestController {
    @Autowired
    private HttpSession session;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TopicService topicService;
    private boolean isTestExpired(LocalDateTime startTestDate, LocalDateTime finishTestDate) {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(startTestDate) || now.isAfter(finishTestDate);
    }
    @GetMapping("/manageTest")
    public String moveToShowQuestion() {
        return "createTestPage";
    }

    @GetMapping("/questionForTest")
    public String getListQuestion(Model model) {
        List<Questions> questions = questionService.getQuestion();
        model.addAttribute("question", questions);
        return "questionForCreateTest";
    }

    @GetMapping("/showQuestionById")
    public String showQuestionById(Model model, @RequestParam("id") int questionId) {
        Questions question = questionRepository.findById(questionId).get();
        model.addAttribute("question", question);
        List<Questions> questionList = questionService.getQuestion();
        model.addAttribute("questionList", questionList);
        return "showDetailQuestion";
    }

    @GetMapping("/test/{topicId}")
    public String getTestForTopic(@PathVariable Integer topicId, Model model) {
        TopicsEntity topic = topicRepository.findById(topicId).orElse(null);
        int total = topic.getTotalQuestion();
        if (isTestExpired(topic.getStartTestDate(), topic.getFinishTestDate())) {
            model.addAttribute("total", total);
            model.addAttribute("topicId", topicId);
            return "redirect:/test/expired";
        }
        List<Questions> selectedQuestions = topicService.selectRandomQuestions();
        model.addAttribute("questions", selectedQuestions);
        return "test";
    }

    @GetMapping("/{topicId}/{totalQuestion}")
    public String getTestForSingleTopic(@PathVariable Integer topicId,
                                        @PathVariable Integer totalQuestion,
                                        Model model) {
        TopicsEntity topic = topicRepository.findById(topicId).orElse(null);
        Integer subjectId = topic.getSubjectId();
        Integer chapterId = topic.getChapter().getChapterId();
        if (isTestExpired(topic.getStartTestDate(), topic.getFinishTestDate())) {
            model.addAttribute("subjectId", subjectId);
            model.addAttribute("chapterId", chapterId);
            model.addAttribute("total", totalQuestion);
            model.addAttribute("topicId", topicId);
            return "accessDenied";
        }
        List<Questions> questions = topicService.findRandomQuestionsByTopicId(topicId, totalQuestion);
        model.addAttribute("questions", questions);
        return "test";

    }

}
