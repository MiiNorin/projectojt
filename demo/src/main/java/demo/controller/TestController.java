package demo.controller;


import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.persistence.entity.TopicsEntity;
import demo.repository.*;
import demo.service.QuestionService;
import demo.service.QuestionTestService;
import demo.service.TestDetailService;
import demo.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private TestDetailService testDetailService;

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

    @Autowired
    QuestionTestRepository questionTestRepository;

    @Autowired
    QuestionTestService questionTestService;
    @GetMapping("/{topicId}/{totalQuestion}")
    public String getTestForSingleTopic(@PathVariable Integer topicId,
                                        @PathVariable Integer totalQuestion,
                                        Model model,
                                        @RequestParam("totalHardQuestion") int totalHardQuestion) {


        int testDetailId = testDetailService.createNewTestDetail(topicId);

        TopicsEntity topic = topicRepository.findById(topicId).orElse(null);
        if (topic == null) {
            // Xử lý trường hợp không tìm thấy topic
            return "topicNotFound";
        }
        //ngăn student vào test quá hạn hoặc chưa mở
        if (isTestExpired(topic.getStartTestDate(), topic.getFinishTestDate())) {
            model.addAttribute("chapterId", topic.getChapter().getChapterId());
            model.addAttribute("total", totalQuestion);
            model.addAttribute("topicId", topicId);
            return "accessDenied";
        }
        //phân loại question và tạo question
        int easyQuestion = totalQuestion - totalHardQuestion;
        List<Questions> hardQuestions = questionRepository.findRandomHardQuestionsByTopicId(topicId, totalHardQuestion);
        List<Questions> randomQuestions = questionRepository.findRandomEasyQuestionsByTopicId(topicId, easyQuestion);

        List<Questions> questions = new ArrayList<>(hardQuestions);
        questions.addAll(randomQuestions);

        //set duration cho bài test
        int duration = topic.getDuration();

        //add data vào model
        List<Questions> selectedQuestions = topicService.findRandomQuestionsByTopicId(topicId, totalQuestion);
        model.addAttribute("questions", selectedQuestions);
        // lack name
        model.addAttribute("testDetailId", testDetailId);
        model.addAttribute("duration",duration);
        model.addAttribute("questions", questions);
        return "test";
    }


}