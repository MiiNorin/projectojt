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
import org.springframework.validation.BindingResult;

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
    public String showAddTopicForm(Model model, @PathVariable("chapterId") int chapterId,
                                   @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("errorMessage", errorMessage);
        return "createListTopic";
    }

    @GetMapping("/showListTopic/{subjectId}/{chapterId}")
    public String showTopicByChapterId(@PathVariable("chapterId") Integer chapterId,
                                       @PathVariable("subjectId") Integer subjectId,
                                       Model model) {
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
                                     @RequestParam String status,
                                     Model model) {
        ChaptersEntity chapter = chapterRepository.findById(chapterId).orElse(null);
        if ((chapter != null) && (totalQuestion > chapter.getTotalQuestion())) {
            model.addAttribute("error", "Total question of the topic should not exceed the total question of the chapter.");
            model.addAttribute("chapterId", chapterId);
            return "createListTopic";
        } else {
            TopicsEntity topicsEntity = new TopicsEntity();
            topicsEntity.setTopicName(topicName);
            topicsEntity.setDuration(duration);
            topicsEntity.setTotalQuestion(totalQuestion);
            topicsEntity.setStatus(status);
            topicsEntity.setChapter(chapter);

            topicRepository.save(topicsEntity);
            return "addChapterSuccess";
        }
    }


    @GetMapping("/showListToAdd")
    public String getTopicsToAdd(Model model) {
        List<TopicsEntity> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
        return "addQuestion";
    }



    @GetMapping("/editTopic")
    public String showEditTopicForm(Model model, @RequestParam("subjectId") int subjectId, @RequestParam("chapterId") int chapterId, @RequestParam("topicId") int topicId) {
        try {
            TopicsEntity topic = topicRepository.findById(topicId).orElse(null);
            if (topic != null) {
                model.addAttribute("topic", topic);
                model.addAttribute("subjectId", subjectId);
                model.addAttribute("chapterId", chapterId);
                return "editTopic";
            } else {
                return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
            }
        } catch (Exception ex) {
            return "error";
        }
    }

    @PostMapping("/editTopic")
    public String editTopic(@RequestParam("chapterId") int chapterId,
                            @RequestParam("subjectId") int subjectId,
                            @RequestParam("topicId") int topicId,
                            @ModelAttribute TopicsEntity topic) {
        try {
            TopicsEntity topics = topicRepository.findById(topicId).orElse(null);
            if (topics != null) {
                topics.setTopicName(topic.getTopicName());
                topics.setDuration(topic.getDuration());
                topics.setTotalQuestion(topic.getTotalQuestion());
                topics.setStatus(topic.getStatus());
                topicRepository.save(topics);
            }
            return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
        } catch (Exception ex) {
            return "redirect:/chapters/showListChapter/" + subjectId;
        }
    }

//    @PostMapping("/editTopic/{id}")
//    public String editTopic(@PathVariable int id, @ModelAttribute TopicsEntity updatedTopic) {
//        updatedTopic.setTopicId(id);
//        topicService.updateTopic(updatedTopic);
//        return "redirect:/listTopics";
//    }

    @GetMapping("/deleteTopic/{id}")
    public String deleteTopic(@PathVariable int id) {
        topicService.deleteTopicById(id);
        return "redirect:/listTopics";
    }


}