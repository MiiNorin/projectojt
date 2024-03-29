package demo.controller;

import demo.persistence.entity.ChaptersEntity;
import demo.repository.ChapterRepository;
import demo.repository.SubjectRepository;
import demo.service.ChapterService;
import demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/addChapter/{subjectId}")
    public String showAddChapterForm(Model model, @PathVariable("subjectId") int subjectId) {
          model.addAttribute("subjectId",subjectId);
        return "addChapter";
    }

    @PostMapping("/addChapter")
    public String addChapterToSubject(@RequestParam("subjectId") int subjectId, @RequestParam String chapterName) {
        ChaptersEntity chaptersEntity = new ChaptersEntity();
        chaptersEntity.setChapterName(chapterName);
        chaptersEntity.setSubjects(subjectRepository.findById(subjectId).get());
        chapterRepository.save(chaptersEntity);
        return "addChapterSuccess";
    }
    @GetMapping("/showListChapter/{subjectId}")
    public String showChaptersBySubjectId(@PathVariable("subjectId") Integer subjectId, Model model) {
        List<ChaptersEntity> chapters = chapterRepository.findChaptersEntityBySubjectsSubjectId(subjectId);
        model.addAttribute("chapters", chapters);
        return "showListChapter";
    }
}
