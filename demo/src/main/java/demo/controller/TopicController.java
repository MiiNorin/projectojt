package demo.controller;

import demo.persistence.entity.*;
import demo.repository.*;
import demo.service.ChapterService;
import demo.service.TopicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@RequestMapping("/listTopics")
@Controller
public class TopicController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SubjectRepository subjectRepository;
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
                                       Model model,
                                       HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId)){
            return "/home/homePage";
        }
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
        for(TopicsEntity topic: topics){
            int topicId = topic.getTopicId();
            List<Questions> questions = questionRepository.findQuestionsByTopicsTopicId(topicId);
            int totalQuestion = topic.getTotalQuestion();
            model.addAttribute("totalQuestions", totalQuestion);
        }
        topicRepository.saveAll(topics);
        Account account = accountRepository.findById(userId).orElse(null);
        model.addAttribute("user", account);
        model.addAttribute("topics", topics);
        Optional<ChaptersEntity> chapterOptional = chapterRepository.findById(chapterId);
        ChaptersEntity chapter = chapterOptional.get();
        model.addAttribute("chapter", chapter);
        model.addAttribute("subject", subjectCheck);
        return "showListTopic";
    }

    @GetMapping("/listTest/{subjectId}/{chapterId}")
    public String showTestForStudent(@PathVariable("chapterId") Integer chapterId,
                                     @PathVariable("subjectId") Integer subjectId,
                                     Model model,
                                     HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(userId).orElse(null);
        SubjectsEntity subjects = subjectRepository.findById(subjectId).orElse(null);
        List<TopicsEntity> topics = topicRepository.findTopicsEntitiesByChapterChapterId(chapterId);
        for(TopicsEntity topic: topics){
            int totalQuestion = topic.getTotalQuestion();
            model.addAttribute("totalQuestions", totalQuestion);
        }

        model.addAttribute("subjects", subjects);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("topics", topics);
        model.addAttribute("user", account);
        System.out.println(account.getFullName());
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
                                   @RequestParam(value = "errorMessage", required = false) String errorMessage,
                                   HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId)){
            return "/home/homePage";
        }
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
                                     Model model,
                                     HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId)){
            return "/home/homePage";
        }
        LocalDateTime now = LocalDateTime.now();
        if (finishTestDate.isBefore(startTestDate) || finishTestDate.isBefore(now.plusSeconds(SOON_SECONDS))) {
            model.addAttribute("errorMessage", "Finish date must be after start date and at least 10 minutes later from now.");
            return "createListTopic";
        }
//        HttpSession httpSession = request.getSession();
//        Map<Integer, Integer> hardQuestionsMap = (Map<Integer, Integer>) httpSession.getAttribute("hardQuestionsMap");
//        if (hardQuestionsMap == null) {
//            hardQuestionsMap = new HashMap<>();
//        }
//        hardQuestionsMap.put(topicId, totalHardQuestions);
//        httpSession.setAttribute("hardQuestionsMap", hardQuestionsMap);

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

    @GetMapping("/editTopic")
    public String showEditTopicForm(Model model,
                                    @RequestParam("subjectId") int subjectId,
                                    @RequestParam("chapterId") int chapterId,
                                    @RequestParam("topicId") int topicId,
                                    HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId || userId!=createdTopicUserId)){
            return "/home/homePage";
        }
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
                            Model model,
                            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId || userId!=createdTopicUserId)){
            return "/home/homePage";
        }
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
                              @PathVariable("topicId") int topicId,
                              HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        Integer createdSubjectUserId = subjectCheck.getAccount().getUserId();
        ChaptersEntity chapterCheck = chapterRepository.findById(chapterId).orElse(null);
        Integer createdChapterUserId = chapterCheck.getSubjects().getAccount().getUserId();
        TopicsEntity topicCheck = topicRepository.findById(topicId).orElse(null);
        Integer createdTopicUserId = topicCheck.getChapter().getSubjects().getAccount().getUserId();
        if(userId!=null && (userId!=createdChapterUserId || userId!=createdSubjectUserId || userId!=createdTopicUserId)){
            return "/home/homePage";
        }
        int count = topicService.countQuestionsByTopicId(topicId);
        topicService.deleteTopicById(topicId);
        ChaptersEntity chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter != null) {
            int currentTotalQuestions = chapter.getTotalQuestion();
            int updatedTotalQuestions = currentTotalQuestions - count;
            chapter.setTotalQuestion(updatedTotalQuestions);
            chapterRepository.save(chapter);
        }

        return "redirect:/listTopics/showListTopic/" + subjectId + "/" + chapterId;
    }


}