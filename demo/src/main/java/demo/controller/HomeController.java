package demo.controller;


import demo.entity.Account;
import demo.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/home")
@Controller
public class HomeController {
    @Autowired
    private AccountRepository accountRepository;
    @GetMapping("/manageQuestion")
    public String moveToShowQuestion(){
        return "mainPage";
    }
    @GetMapping("/homePage")
    public String homePage(Model model, HttpSession session) {
        // Kiểm tra xem user_id có tồn tại trong session hay không
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        return "homePage";
    }
    @PostMapping("/homePage")
    public String headerUser(Model model, HttpSession session) {
        // Kiểm tra xem user_id có tồn tại trong session hay không
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        return "headerUser";
    }

//    @GetMapping("/homePage")
//    public String moveToHomePage(){
//        return "homePage";
//    }
    @GetMapping("/About")
    public String moveToAboutPage(){
        return "about";
    }
    @GetMapping("/Contact")
    public String moveToContactPage(){
        return "contact";
    }

}
