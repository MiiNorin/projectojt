package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListAccount {

    @GetMapping("/linkAccount")
    public String showLinkAccountPage() {
        return "LinkAccount"; // Trả về tên của file HTML template cho trang "LinkAccount"
    }
}
