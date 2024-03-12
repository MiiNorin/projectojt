package WebApplication.Controller;

import WebApplication.Entity.TopicsEntity;
import WebApplication.Services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/listTopics")
    public String getTopics(Model model) {
        List<TopicsEntity> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "ListTopic";
    }

    @PostMapping("/addTopic")
    public String addTopic(@ModelAttribute TopicsEntity newTopic) {
        topicService.saveTopic(newTopic);
        return "redirect:/listTopics";
    }

    @GetMapping("/editTopic/{id}")
    public String editTopicForm(@PathVariable int id, Model model) {
        Optional<TopicsEntity> topic = topicService.getTopicById(id);
        topic.ifPresent(value -> model.addAttribute("topic", value));
        return "editTopic";
    }

    @PostMapping("/editTopic/{id}")
    public String editTopic(@PathVariable int id, @ModelAttribute TopicsEntity updatedTopic) {
        updatedTopic.setTopicId(id);
        topicService.updateTopic(updatedTopic);
        return "redirect:/listTopics";
    }

    @GetMapping("/deleteTopic/{id}")
    public String deleteTopic(@PathVariable int id) {
        topicService.deleteTopicById(id);
        return "redirect:/listTopics";
    }
}
