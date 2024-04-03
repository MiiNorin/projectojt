package demo.controller;


import demo.entity.Account;
import demo.entity.Role;
import demo.repository.AccountRepository;
import demo.repository.RoleRepository;
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
    @Autowired
    private RoleRepository roleRepository;
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
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
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
            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("role", role);
            }
        }
        return "headerUser";
    }
    @GetMapping("/About")
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
    @GetMapping("/Trainers")
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
    @GetMapping("/Contact")
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
    @GetMapping("/Profile")
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
