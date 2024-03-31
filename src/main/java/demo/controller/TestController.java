package demo.controller;


import demo.entity.Questions;
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
    @GetMapping("/manageTest")
    public String moveToShowQuestion(){
        return "readonlyQuestion";
    }

}
