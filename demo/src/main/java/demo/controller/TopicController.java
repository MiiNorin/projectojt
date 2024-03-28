package demo.controller;

import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.Questions;
import demo.persistence.entity.TopicsEntity;
import demo.repository.TopicRepository;
import demo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Topic;
import javax.validation.Valid;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RequestMapping("/listTopics")
@Controller
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicService topicService;

    @GetMapping("/showList")
    public String getTopics(Model model) {
        List<TopicsEntity> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "ListTopic";
    }

    @GetMapping("/addTopic")
    public String showTopicPage(Model model) {
        TopicsEntity topics = new TopicsEntity();
        model.addAttribute("topic", topics);
        return "createListTopic";
    }
    @GetMapping("/showListToAdd")
    public String getTopicsToAdd(Model model) {
        List<TopicsEntity> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "addQuestion";
    }
    @PostMapping("/addTopic")
    public String createTopic(
            @Valid @ModelAttribute TopicsEntity topics,
            BindingResult result) {

        TopicsEntity topicsEntity = new TopicsEntity();
        topicsEntity.setTopicName(topics.getTopicName());
        topicsEntity.setDuration(topics.getDuration());
        topicsEntity.setTotalQuestion(topics.getTotalQuestion());
        topicsEntity.setTopicType(topics.getTopicType());
        topicsEntity.setStatus(topics.getStatus());
        LocalDateTime createDate = LocalDateTime.now();
        topicsEntity.setCreateDate(createDate);
        topicRepository.save(topicsEntity);
        return "redirect:/listTopics/showList";
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


    @GetMapping("test/{topicId}")
    public String getTestForTopic(@PathVariable Integer topicId, Model model) {
        List<Questions> selectedQuestions = topicService.getRandomQuestionsByTopicId(topicId);
        model.addAttribute("questions", selectedQuestions);
        return "test";
    }
}