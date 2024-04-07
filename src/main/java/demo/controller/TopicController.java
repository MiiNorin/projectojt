package demo.controller;

import demo.entity.Topics;
import demo.repository.AccountRepository;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.security.auth.Subject;
import java.util.List;

@Controller
public class TopicController {
    @Autowired
    TopicRepository topicRepository;


    @GetMapping("/topic")
    public String getAllAccount(Model model){
        List<Topics> topicsList = topicRepository.findAll();
        model.addAttribute("topicsList", topicsList);
        return "TopicManager";
    }

}
