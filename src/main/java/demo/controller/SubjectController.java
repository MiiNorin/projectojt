package demo.controller;

import demo.entity.Account;
import demo.entity.Subjects;

import demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SubjectController {
    @Autowired
    SubjectRepository subject;
    @GetMapping("/subject")
    public String getAllSubject(Model model){
        List<Subjects> subjectList = subject.findAll();
        model.addAttribute("subjectList", subjectList);
        return "SubjectManger";
    }

//    @GetMapping("/home/cc")
//    public String getAllSubjects(Model model){
//        List<Subjects> subjectsList = subject.findAll();
//        model.addAttribute("subjectsList", subjectsList);
//        return "ViewAdmin";
//    }

}
