package demo.controller;


import demo.persistence.entity.SubjectsEntity;
import demo.persistence.entity.TopicsEntity;
import demo.repository.SubjectRepository;
import demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RequestMapping("/subject")
@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping("/listSubjects")
    public String getSubjects(Model model) {
        List<SubjectsEntity> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "ListSubject";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectsEntity newSubject) {
        SubjectsEntity subjectsEntity = new SubjectsEntity();
        subjectsEntity.setSubjectName(newSubject.getSubjectName());
        subjectsEntity.setImgLink(newSubject.getImgLink());
        LocalDateTime createDate = LocalDateTime.now();
        subjectsEntity.setCreateDate(createDate);
        subjectsEntity.setSlot(newSubject.getSlot());
        subjectRepository.save(subjectsEntity);
        return "addSubjectSuccess";
    }

    @GetMapping("/subject/editSubject/{id}")
    public String editSubjectForm(@PathVariable int id, Model model) {
        Optional<SubjectsEntity> subject = subjectService.getSubjectById(id);
        subject.ifPresent(value -> model.addAttribute("subject", value));
        return "editSubject";
    }

    @PostMapping("/subject/editSubject/{id}")
    public String editSubject(@PathVariable int id, @ModelAttribute SubjectsEntity updatedSubject) {
        updatedSubject.setSubjectId(id);
        subjectService.updateSubject(updatedSubject);
        return "redirect:/listSubjects";
    }

    @GetMapping("/deleteSubject")
    public String deleteSubject(@RequestParam("id") int id) {
        subjectRepository.deleteById(id);
        return "redirect:/subject/listSubjects";
    }

}