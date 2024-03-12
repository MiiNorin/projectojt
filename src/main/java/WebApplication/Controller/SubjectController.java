package WebApplication.Controller;

import WebApplication.Entity.SubjectsEntity;
import WebApplication.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/listSubjects")
    public String getSubjects(Model model) {
        List<SubjectsEntity> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "ListSubject";
    }

    @PostMapping("/addSubject")
    public String addSubject(@ModelAttribute SubjectsEntity newSubject) {
        subjectService.saveSubject(newSubject);
        return "redirect:/listSubjects";
    }

    @GetMapping("/editSubject/{id}")
    public String editSubjectForm(@PathVariable int id, Model model) {
        Optional<SubjectsEntity> subject = subjectService.getSubjectById(id);
        subject.ifPresent(value -> model.addAttribute("subject", value));
        return "editSubject";
    }

    @PostMapping("/editSubject/{id}")
    public String editSubject(@PathVariable int id, @ModelAttribute SubjectsEntity updatedSubject) {
        updatedSubject.setSubjectId(id);
        subjectService.updateSubject(updatedSubject);
        return "redirect:/listSubjects";
    }

    @GetMapping("/deleteSubject/{id}")
    public String deleteSubject(@PathVariable int id) {
        subjectService.deleteSubjectById(id);
        return "redirect:/listSubjects";
    }
}
