package demo.controller;

import demo.persistence.entity.Account;
import demo.repository.AccountRepository;
import demo.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

//        Account user = accountRepository.findByEmail(username).orElse(null);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//
//            System.out.println("user Id get success");
//
//            model.addAttribute("user", user);
//            return "redirect:/home/homePage";
        Optional<Account> user = accountRepository.findByEmail(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
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
    @GetMapping("/changePassword")
    public String ChangePassLogin(@RequestParam("username") String email, Model model, HttpSession session1, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Account account = accountRepository.findAccountByEmail(email);
            model.addAttribute("account",account);
            return "changePassword";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @PostMapping("/changePassword")
    public String ChangePassLoginDone(@RequestParam("email") String email,
                                      @RequestParam("newPass") String newPass,
                                      @RequestParam("renewPass") String renewPass,
                                      RedirectAttributes redirectAttributes,
                                      Model model, HttpSession session1, HttpServletRequest request) {
        Account account = accountRepository.findAccountByEmail(email);
        System.out.println(account);
        String result = accountService.changePasswordLogin(account.getUserId(),newPass,renewPass);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/FAcademy/changePassword";

    }


}