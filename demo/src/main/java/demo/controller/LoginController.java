package demo.controller;

import demo.persistence.entity.Account;
import demo.repository.AccountRepository;
import demo.service.AccountService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@Controller
@AllArgsConstructor
@SessionAttributes("accountDto")
@RequestMapping("/FAcademy")
public class LoginController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/loginPage")
    String login() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {

        Account user = accountRepository.findByEmail(username).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            int accountId = user.getUserId();
//            session.setAttribute("accountId", accountId);
//            model.addAttribute("accountId", accountId);
            System.out.println("user Id get success");

            model.addAttribute("user", user);
            return "redirect:/home/homePage";
        } else {
            model.addAttribute("errorlogin", "Invalid username or password");
            return "login";
        }
    }
    @GetMapping("/forgotPassPage")
    String forgotPass() {
        return "forgotPass";
    }
    @PostMapping("/forgotPassPage")
    public String forgotPass(@RequestParam String username, Model model) {
        Optional<Account> user = accountRepository.findByEmail(username);
        if (user.isPresent() ) {
            return "redirect:/home/homePage";
        } else {
            // auth failed, set error message and return to login page
            model.addAttribute("errorlogin", "Invalid username");
            return "forgotPass";
        }


    }


}