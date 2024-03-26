package demo.controller;

import demo.entity.Account;
import demo.entity.Questions;
import demo.repository.AccountRepository;
import demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/account")
@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/showAccount")
    public String getListAccount(Model model)
    {
        List<Account> account = accountService.getAccount();
        model.addAttribute("account", account);
        return "userList";

    }
    @GetMapping("/Trainers")
    String Trainers() {
        return "trainers";
    }
}
