package demo.controller;

import demo.entity.Account;

import demo.entity.Questions;
import demo.entity.Subjects;
import demo.entity.Topics;
import demo.repository.AccountRepository;
import demo.repository.QuestionRepository;
import demo.repository.SubjectRepository;
import demo.repository.TopicRepository;
import demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.security.auth.Subject;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    AccountRepository account;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    private AccountService accountService;
    @GetMapping("/account")
    public String getAllAccount(Model model){
        List<Account> accountList = account.findAll();
        model.addAttribute("accountList", accountList);
        return "AccountManager";
    }
    @GetMapping("/account/deleteAccount/{id}")
    public String deleteAccount(@PathVariable("id") Long id, Model model) {
        accountService.deleteAccountById(id);
        List<Account> accountList = account.findAll();
        model.addAttribute("accountList", accountList);
        return "AccountManager";
    }
    @GetMapping("/home/cc")
    public String getAllAccounts(Model model){
        List<Account> accountList = account.findAll();
        List<Subjects> subjectsList = subjectRepository.findAll();
        List<Questions> questionList = questionRepository.findAll();
        List<Topics> topicsList = topicRepository.findAll();
        model.addAttribute("subjectList", subjectsList);
        model.addAttribute("accountList", accountList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("topicsList", topicsList);
        return "ViewAdmin";
    }

}