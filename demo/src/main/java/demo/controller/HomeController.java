package demo.controller;

import demo.persistence.entity.Account;
import demo.persistence.entity.Role;
import demo.repository.AccountRepository;
import demo.repository.RoleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@Controller
public class HomeController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String redirectToHomePage(Model model, HttpSession session) {
        return "redirect:/home/homePage";
    }

    @GetMapping("/home/manageQuestion")
    public String moveToShowQuestion(){
        return "mainPage";
    }

    @GetMapping("/home/homePage")
    public String homePage(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findById(userId).orElse(null);
        System.out.println(account.getFullName());
        if (userId != null) {
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
                model.addAttribute("userId", userId);
            }
        }
        return "homePage";
    }

    @PostMapping("/home/homePage")
    public String headerUser(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "headerUser";
    }

    @GetMapping("/home/About")
    public String moveToAboutPage(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "about";
    }

    @GetMapping("/home/Trainers")
    String Trainers(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "trainers";
    }

    @GetMapping("/home/Contact")
    public String moveToContactPage(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "contact";
    }

    @GetMapping("/home/Profile")
    public String moveToProfilePage(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userId != null) {
            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
            Account user = accountRepository.findById(userId).orElse(null);
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "user-profile";
    }
}
