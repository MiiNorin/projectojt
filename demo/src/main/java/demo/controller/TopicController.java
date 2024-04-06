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

import java.time.LocalDateTime;
import java.time.Period;
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
    private static final int SOON_SECONDS = 3;


    @GetMapping("/showListTopic/{subjectId}/{chapterId}")
    public String showTopicByChapterId(@PathVariable("chapterId") Integer chapterId,
                                       @PathVariable("subjectId") Integer subjectId,
                                       Model model) {
        List<TopicsEntity> topics = topicRepository.findTopicsEntitiesByChapterChapterId(chapterId);
        LocalDateTime now = LocalDateTime.now();
        for (TopicsEntity topic : topics) {
            if (now.isBefore(topic.getStartTestDate().minusMinutes(SOON_SECONDS))) {
                topic.setStatus("Soon");

            } else if (now.isBefore(topic.getFinishTestDate())) {
                topic.setStatus("OnGoing");
            } else {
                topic.setStatus("Finished");
            }
        }
        topicRepository.saveAll(topics);
        model.addAttribute("topics", topics);
        Optional<ChaptersEntity> chapterOptional = chapterRepository.findById(chapterId);
        ChaptersEntity chapter = chapterOptional.get();
        model.addAttribute("chapter", chapter);
        return "showListTopic";
    }

    @GetMapping("/listTest/{subjectId}/{chapterId}")
    public String showTestForStudent(@PathVariable("chapterId") Integer chapterId,
                                     @PathVariable("subjectId") Integer subjectId,
                                     Model model) {
        List<TopicsEntity> topics = topicRepository.findTopicsEntitiesByChapterChapterId(chapterId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("topics", topics);
        Optional<ChaptersEntity> chapterOptional = chapterRepository.findById(chapterId);
        ChaptersEntity chapter = chapterOptional.get();
        model.addAttribute("chapter", chapter);
        return "showListTestForStudent";
    }

    @GetMapping("/test/{chapterId}")
    public String showQuestionByChapterId(@PathVariable("chapterId") Integer chapterId, Model model) {
        List<Questions> questions = questionRepository.findQuestionsByChaptersChapterId(chapterId);
        model.addAttribute("questions", questions);
        return "test";
    }
    @GetMapping("/addTopic/{subjectId}/{chapterId}")
    public String showAddTopicForm(Model model, @PathVariable("subjectId") int subjectId, @PathVariable("chapterId") int chapterId,
                                   @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("errorMessage", errorMessage);
        return "createListTopic";
    }

    @PostMapping("/addTopic")
    public String addTopicForChapter(@RequestParam("subjectId") int subjectId,
                                     @RequestParam("chapterId") int chapterId,
                                     @RequestParam String topicName,
                                     @RequestParam int duration,
                                     @RequestParam int totalQuestion,
                                     @RequestParam String status,
                                     @RequestParam("startTestDate") LocalDateTime startTestDate,
                                     @RequestParam("finishTestDate") LocalDateTime finishTestDate,
                                     Model model) {
        LocalDateTime now = LocalDateTime.now();
        if (finishTestDate.isBefore(startTestDate) || finishTestDate.isBefore(now.plusSeconds(SOON_SECONDS))) {
            model.addAttribute("errorMessage", "Finish date must be after start date and at least 10 minutes later from now.");
            return "createListTopic";
        }
        ChaptersEntity chapter = chapterRepository.findById(chapterId).orElse(null);

        TopicsEntity topicsEntity = new TopicsEntity();
        topicsEntity.setTopicName(topicName);
        topicsEntity.setDuration(duration);
        topicsEntity.setTotalQuestion(totalQuestion);
        topicsEntity.setStatus(status);
        topicsEntity.setCreateDate(LocalDateTime.now());
        topicsEntity.setStartTestDate(startTestDate);
        topicsEntity.setFinishTestDate(finishTestDate);
        topicsEntity.setChapter(chapter);
        topicsEntity.setSubjectId(subjectId);
        topicRepository.save(topicsEntity);
        topicsEntity.setSubjectId(subjectId);
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("subjectId", subjectId);
        return "addChapterSuccess";
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
                            @ModelAttribute TopicsEntity topic,
                            Model model) {
        try {
            TopicsEntity topics = topicRepository.findById(topicId).orElse(null);
            if (topics != null) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startTestDate = topic.getStartTestDate();
                LocalDateTime finishTestDate = topic.getFinishTestDate();

                // Kiểm tra FinishDate phải sau StartDate và ít nhất là 10 phút
                if (finishTestDate.isBefore(startTestDate) || finishTestDate.isBefore(now.plusSeconds(SOON_SECONDS))) {
                    model.addAttribute("errorMessage", "Finish date must be after start date and at least 10 minutes later from now.");
                    return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
                }
                topics.setTopicName(topic.getTopicName());
                topics.setDuration(topic.getDuration());
                topics.setTotalQuestion(topic.getTotalQuestion());
                topics.setStatus(topic.getStatus());
                topics.setStartTestDate(topic.getStartTestDate());
                topics.setFinishTestDate(topic.getFinishTestDate());
                topicRepository.save(topics);
            }
            return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
        } catch (Exception ex) {
            return "redirect:/chapters/showListChapter/" + subjectId;
        }
    }

    @GetMapping("/deleteTopic/{subjectId}/{chapterId}/{topicId}")
    public String deleteTopic(@PathVariable("subjectId") int subjectId,
                              @PathVariable("chapterId") int chapterId,
                              @PathVariable("topicId") int topicId) {
        topicService.deleteTopicById(topicId);
        return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
    }


}