package demo.controller;

import demo.persistence.dto.QuestionDto;
import demo.persistence.entity.Account;
import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import demo.repository.AccountRepository;
import demo.repository.ChapterRepository;
import demo.repository.SubjectRepository;
import demo.service.ChapterService;
import demo.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private AccountRepository accountRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectService subjectService;
    @GetMapping("/ChapterPage")
    public String ChapterPage(){
        return "ChapterPage";
    }
    @GetMapping("/showListChapter")
    public String showChaptersBySubjectId(@RequestParam("subjectId") Integer subjectId,
                                          @RequestParam(defaultValue = "0") int page,
                                          Model model,
                                          HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(userId).orElse(null);
        model.addAttribute("user", account);
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
//        List<ChaptersEntity> chapters = chapterRepository.findChaptersEntityBySubjectsSubjectId(subjectId);
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("chapterName").descending());
        Page<ChaptersEntity> chapters = chapterRepository.findBySubjectsSubjectId(subjectId, pageable);
        model.addAttribute("chapters", chapters.getContent());
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("userId", userId);
        model.addAttribute("currentPage", page);
        model.addAttribute("subjects", subjectCheck);
        model.addAttribute("totalPages", chapters.getTotalPages());
        model.addAttribute("user", account);
        Optional<SubjectsEntity> subjects = subjectRepository.findById(subjectId);
        SubjectsEntity subject = subjects.get();
        model.addAttribute("subject", subject);
        return "showListChapter";
    }
    @PostMapping("/")
    public String getUserId(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("user_id");
        model.addAttribute("userId", userId);
        return "layouts/Admin/SidebarChapter";
    }



    @GetMapping("/addChapter/{subjectId}")
    public String showAddChapterForm(Model model, @PathVariable("subjectId") int subjectId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(userId).orElse(null);
        if (userId == null) {
            return "redirect:/home/homePage";
        }
        SubjectsEntity subjectCheck = subjectRepository.findById(subjectId).orElse(null);
        int createdSubjectUserId = subjectCheck.getAccount().getUserId();
        if(userId!=null && userId!=createdSubjectUserId){
            return "redirect:/home/homePage";
        }
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("user", account);
        model.addAttribute("userId", userId);
        model.addAttribute("subjects", subjectCheck);

        return "addChapter";
    }

    @PostMapping("/addChapter")
    public String addChapterToSubject(Model model, @RequestParam("subjectId") int subjectId,
                                      @RequestParam String chapterName, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(userId).orElse(null);
        int subjectIdToCheck = subjectId;
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
        model.addAttribute("user", account);
        chapterRepository.save(chaptersEntity);
        return "redirect:/chapters/showListChapter?subjectId=" + subjectIdToCheck;
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
