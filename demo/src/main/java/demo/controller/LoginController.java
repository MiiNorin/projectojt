package demo.controller;


import demo.entity.Account;
import demo.repository.AccountRepository;
import demo.service.AccountService;
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
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        Optional<Account> user = accountRepository.findByEmail(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // auth successful, redirect to the product page
            return "redirect:/home/homePage";
        } else {
            // auth failed, set error message and return to login page
            model.addAttribute("errorlogin", "zInvalid username or password");
            return "login";
        }
    }
}
