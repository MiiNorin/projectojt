package demo.controller;

import demo.entity.Account;
import demo.entity.Subjects;

import demo.repository.SubjectRepository;
import demo.service.AccountService;
import demo.service.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SubjectController {
    @Autowired
    SubjectRepository subject;
    @Autowired
    private SubjectsService subjectsService;
    @GetMapping("/subject")
    public String getAllSubject(Model model){
        List<Subjects> subjectList = subject.findAll();
        model.addAttribute("subjectList", subjectList);
        return "SubjectManger";
    }
    @GetMapping("/subject/deleteSubject/{id}")
    public String deleteAccount(@PathVariable("id") Long id) {
        subjectsService.deleteSubjectById(id);
        return "redirect:/subject/SubjectManager";
    }


}
