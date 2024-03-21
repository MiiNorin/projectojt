package demo.controller;


import demo.persistence.entity.Questions;
import demo.repository.QuestionRepository;
import demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/test")
@Controller
public class TestController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @GetMapping("/manageTest")
    public String moveToShowQuestion(){
        return "createTestPage";
    }

    @GetMapping("/questionForTest")
    public String getListQuestion(Model model)
    {
        List<Questions> questions = questionService.getQuestion();
        model.addAttribute("question", questions);
        return "questionForCreateTest";
    }
    @GetMapping("/showQuestionById")
    public String showQuestionById(Model model, @RequestParam("id") int questionId){
        Questions question = questionRepository.findById(questionId).get();
        model.addAttribute("question", question);
        List<Questions> questionList = questionService.getQuestion();
        model.addAttribute("questionList", questionList);
        return "showDetailQuestion";
    }
}
