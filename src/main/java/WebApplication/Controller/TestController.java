package WebApplication.Controller;

import WebApplication.Entity.QuestionsEntity;
import WebApplication.Services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/listTopics")
public class TestController {
    @Autowired
    private TopicService topicService;
//    @GetMapping("/test/{topicId}")
//    public String getTestForTopic(@PathVariable Integer topicId, Model model) {
//
//        // Step 1: Retrieve random questions
//        List<QuestionsEntity> selectedQuestions = topicService.selectRandomQuestions();
//
//        // Step 2: Add selected questions to the model
//        model.addAttribute("questions", selectedQuestions);
//
//        // Step 3: Return the name of the view to display the test
//        return "test";
//    }
}
