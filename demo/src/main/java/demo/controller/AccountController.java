package demo.controller;

import demo.persistence.entity.AccountEntity;
import demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accountList")
    public String getAccountList(Model model) {
        List<AccountEntity> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "accountList"; // Trả về tên của trang JSP
    }

}