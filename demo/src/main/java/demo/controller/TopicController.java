package demo.controller;

import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.Questions;
import demo.persistence.entity.TopicsEntity;
import demo.repository.ChapterRepository;
import demo.repository.QuestionRepository;
import demo.repository.TopicRepository;
import demo.service.ChapterService;
import demo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RequestMapping("/listTopics")
@Controller
public class TopicController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterService chapterService;
//    @GetMapping("/showList")
//    public String getTopics(Model model) {
//        List<TopicsEntity> topics = topicService.getAllTopics();
//        model.addAttribute("topics", topics);
//        return "shotListTopic";
//    }
    @GetMapping("/addTopic/{chapterId}")
    public String showAddTopicForm(Model model, @PathVariable("chapterId") int chapterId) {
        model.addAttribute("chapterId",chapterId);
        return "createListTopic";
    }

    @GetMapping("/showListTopic/{chapterId}")
    public String showTopicByChapterId(@PathVariable("chapterId") Integer chapterId, Model model) {
        List<TopicsEntity> topics = topicRepository.findTopicsEntitiesByChapterChapterId(chapterId);
        model.addAttribute("topics", topics);
        return "showListTopic";
    }
    @GetMapping("/test/{chapterId}")
    public String showQuestionByChapterId(@PathVariable("chapterId") Integer chapterId, Model model) {
        List<Questions> questions = questionRepository.findQuestionsByChaptersChapterId(chapterId);
        model.addAttribute("questions", questions);
        return "test";
    }
    @PostMapping("/addTopic")
    public String addTopicForChapter(@RequestParam("chapterId") int chapterId, @RequestParam String topicName,
                                     @RequestParam int duration,
                                     @RequestParam int totalQuestion,
                                     @RequestParam String status) {
        TopicsEntity topicsEntity = new TopicsEntity();
        topicsEntity.setTopicName(topicName);
        topicsEntity.setDuration(duration);
        topicsEntity.setTotalQuestion(totalQuestion);
        topicsEntity.setStatus(status);
        topicsEntity.setChapter(chapterRepository.findById(chapterId).get());
        topicRepository.save(topicsEntity);
        return "addChapterSuccess";
    }


    @GetMapping("/showListToAdd")
    public String getTopicsToAdd(Model model) {
        List<TopicsEntity> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "addQuestion";
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