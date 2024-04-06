package demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestDetailController {
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user_id");
        session.removeAttribute("user");
        session.removeAttribute("role");
        System.err.println("red");
        return "redirect:/home/homePage";
    }
}
