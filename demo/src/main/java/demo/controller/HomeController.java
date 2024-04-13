package demo.controller;

import demo.persistence.dto.AccountDto;
import demo.persistence.entity.Account;
import demo.persistence.entity.Role;
import demo.repository.AccountRepository;
import demo.repository.RoleRepository;
import demo.service.AccountService;
import demo.service.FileUpload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/home")
@Controller
@RequiredArgsConstructor
public class HomeController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleRepository roleRepository;
    private final FileUpload fileUpload;
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
    @PostMapping("/editProfile")
    public String editProfile(Model model, HttpSession session,
                              @RequestParam("avatar") MultipartFile multipartFile,
                              @RequestParam("fullName") String fullName,
                              @RequestParam("phone") String phone,
                              @RequestParam("email") String email,
                              @RequestParam("school") String school,
                              @RequestParam("gender") String gender,
                              RedirectAttributes redirectAttributes,
                              @Valid @ModelAttribute AccountDto accountDto, BindingResult result){
        try {
            Integer userId = (Integer) session.getAttribute("user_id");
            String imageURL = fileUpload.uploadFile(multipartFile);
            model.addAttribute("image", imageURL);
            accountService.updateAccount(imageURL, fullName, phone, email, school, gender,userId);
            System.out.println(fullName);
            redirectAttributes.addFlashAttribute("message", "Account updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error updating account.");
        }

        return "redirect:/home/Profile";
    }
    @PostMapping("/changePassword")
    public String changePassword(Model model, HttpSession session, RedirectAttributes redirectAttributes,
                                 @RequestParam("password") String currentPass,
                                 @RequestParam("newpassword") String newPass,
                                 @RequestParam("renewpassword") String renewPass)
    {
        // Giả sử bạn đã xác thực người dùng và lấy được userId từ session hoặc security context
        Integer userId = (Integer) session.getAttribute("user_id"); // Lấy userId thực tế từ session hoặc security context

        String result = accountService.changePassword(userId, currentPass,newPass,renewPass);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/home/Profile"; // Redirect để tránh duplicate submission
    }

}