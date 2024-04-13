package demo.controller;


import demo.entity.Account;
import demo.repository.AccountRepository;
import demo.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.sql.Timestamp;
import java.util.Map;
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
            return "redirect:/home/homePage";
        } else {
            // auth failed, set error message and return to login page
            model.addAttribute("errorlogin", "Invalid username or password");
            return "login";
        }
    }
    @GetMapping("/forgotPassPage")
    String fogotPass() {
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
//    @GetMapping("/signingoogle")
//    public String currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
//        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
//
//        // Convert attributes to User object
//        Account user = toUser(attributes);
//        if (!accountRepository.existsAccountByEmail(user.getEmail())) {
//            accountRepository.save(user);
//        }
//        return "redirect:/home/homepage";
//    }
//
//    public Account toUser(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        }
//        Account user = new Account();
//        user.setEmail((String) map.get("email"));
//        user.setFullName((String) map.get("name"));
//        user.setRoleId(4);
//        user.setPassword("");
//        user.setPhone("");
//        user.setBirthDay(Timestamp.valueOf("1990-01-01 00:00:00"));
//        accountRepository.save(user);
//        return user;
//    }
}
