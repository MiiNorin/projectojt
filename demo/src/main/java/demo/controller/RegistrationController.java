package demo.controller;

import demo.entity.AccountDto;
import demo.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/FAcademy")
public class RegistrationController {
    private AccountService accountService;
    @ModelAttribute("accountdto")
    public AccountDto userResgistrationDto() { return new AccountDto(); }
    @GetMapping("/registration")
    public String showRegistrationForm() { return "registration"; }
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("accountdto") AccountDto accountDto, Model model,HttpSession session) {
        if(accountService.checkAccountByEmail(accountDto.getEmail())){
            model.addAttribute("error","Email is already used!");
            return "redirect:/FAcademy/registration?emailexist";
        }
        if(accountDto.getPassword().equals(accountDto.getCheckPass())==false){
            model.addAttribute("error","Password is incorrect");
            return "redirect:/FAcademy/registration?checkpass";
        }
        accountService.save(accountDto,session);
        return "otp_verify";
    }
//    @PostMapping("/register")
//    public String register( RegisterRequest registerRequest, Model model) {
//        boolean registerResponse = userService.register(registerRequest);
//        if(!registerResponse)
//        {
//            model.addAttribute("error","Email is already used!");
//            return "registration";
//        } else
//        {
//            return "otp_verify";
//        }
//    }

    @PostMapping("/verify")
    public String verifyUser(@RequestParam String email, @RequestParam("otp") String otp, Model model,HttpSession session1,HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            accountService.verify(email, otp, session);
            return "/FAcademy/login?registerSuccess";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
