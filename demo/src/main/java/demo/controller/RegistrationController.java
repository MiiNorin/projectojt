package demo.controller;

import demo.entity.AccountDto;
import demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private AccountService accountService;
    @ModelAttribute("accountdto")
    public AccountDto userResgistrationDto() { return new AccountDto(); }
    @GetMapping("/registration")
    public String showRegistrationForm() { return "registration"; }
    @PostMapping("/registrations")
    public String registerUserAccount(@ModelAttribute("accountdto") AccountDto accountDto) {
        if(accountService.checkAccountByEmail(accountDto.getEmail())){
            return "redirect:/registration?emailexist";
        }
        if(accountDto.getPassword().equals(accountDto.getCheckPass())==false){
            return "redirect:/registration?checkpass";
        }
        accountService.save(accountDto);
        return "redirect:/registration?success";
        }
}
