package demo.controller;

import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.SubjectsEntity;
import demo.service.ChapterService;
import demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/addChapter/{subjectId}")
    public String getCreateChapterForSubject(){
        return "addChapter";
    }

    @PostMapping("/addChapter/{subjectId}")
    public String createChapterForSubject(@PathVariable int subjectId, @RequestBody ChaptersEntity chapter) {
        SubjectsEntity subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + subjectId));

        chapter.setSubjectId(subjectId);
        chapterService.saveChapter(chapter);
        return "addChapter";
    }

}
