package demo.controller;

import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.repository.ChapterRepository;
import demo.repository.SubjectRepository;
import demo.service.ChapterService;
import demo.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chapters")
public class ChapterController {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectService subjectService;
    @GetMapping("/showListChapter")
    public String showChaptersBySubjectId(@RequestParam("subjectId") Integer subjectId, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        List<ChaptersEntity> chapters = chapterRepository.findChaptersEntityBySubjectsSubjectId(subjectId);
        model.addAttribute("chapters", chapters);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("userId", userId);
        Optional<SubjectsEntity> subjects = subjectRepository.findById(subjectId);
        SubjectsEntity subject = subjects.get();
        model.addAttribute("subject", subject);
        return "showListChapter";
    }


    @GetMapping("/chooseChapter")
    public String showChapterForStudent(@RequestParam("subjectId") Integer subjectId, Model model) {
        List<ChaptersEntity> chapters = chapterRepository.findChaptersEntityBySubjectsSubjectId(subjectId);
        model.addAttribute("chapters", chapters);
        model.addAttribute("subjectId", subjectId);
        SubjectsEntity subject = subjectRepository.findById(subjectId).orElse(null);
        model.addAttribute("subject", subject);
        return "showListChapterForStudent";
    }

    @GetMapping("/addChapter/{subjectId}")
    public String showAddChapterForm(Model model, @PathVariable("subjectId") int subjectId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        model.addAttribute("subjectId", subjectId);
        return "addChapter";
    }

    @PostMapping("/addChapter")
    public String addChapterToSubject(Model model, @RequestParam("subjectId") int subjectId,
                                      @RequestParam String chapterName, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        ChaptersEntity chaptersEntity = new ChaptersEntity();
        chaptersEntity.setChapterName(chapterName);
        int totalQuestionInChapter = 0;
        chaptersEntity.setTotalQuestion(totalQuestionInChapter);
        model.addAttribute("totalQuestion", totalQuestionInChapter);
        chaptersEntity.setSubjects(subjectRepository.findById(subjectId).get());
        SubjectsEntity subjects = subjectRepository.findById(subjectId).orElse(null);
        model.addAttribute("subjectId", subjectId);
        chapterRepository.save(chaptersEntity);
        return "addChapterSuccess";
    }


    @GetMapping("/deleteChapter/{chapterId}/{subjectId}")
    public String deleteChapter(@PathVariable("chapterId") int chapterId, @PathVariable("subjectId") int subjectId,
                                HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        chapterRepository.deleteById(chapterId);
        return "redirect:/chapters/showListChapter?subjectId=" + subjectId;
    }

    @GetMapping("/editChapter")
    public String showEditChapterForm(Model model,
                                      @RequestParam("subjectId") int subjectId,
                                      @RequestParam("chapterId") int chapterId,
                                      HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        try {
            ChaptersEntity chapter = chapterRepository.findById(chapterId).orElse(null);
            if (chapter != null) {
                model.addAttribute("chapter", chapter);
                model.addAttribute("subjectId", subjectId);
                return "editChapter";
            } else {
                return "redirect:/chapters/showListChapter/" + subjectId;
            }
        } catch (Exception ex) {
            return "error";
        }
    }

    @PostMapping("/editChapter")
    public String editQuestion(@RequestParam("chapterId") int chapterId,
                               @RequestParam("subjectId") int subjectId,
                               @ModelAttribute ChaptersEntity chapter,
                               HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        try {
            ChaptersEntity chapters = chapterRepository.findById(chapterId).orElse(null);
            if (chapters != null) {
                chapters.setChapterName(chapter.getChapterName());
                chapterRepository.save(chapters);
            }
            return "redirect:/chapters/showListChapter?subjectId=" + subjectId;
        } catch (Exception ex) {
            return "redirect:/chapters/showListChapter/" + subjectId;
        }
    }

}
