package demo.controller;


import demo.entity.Questions;
import demo.repository.QuestionRepository;
import demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/home")
@Controller
public class HomeController {
    @GetMapping("/manageQuestion")
    public String moveToShowQuestion(){
        return "mainPage";
    }

}
