package demo.controller;

import demo.entity.*;
import demo.repository.*;
import demo.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@RequestMapping("/account")
@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    SubjectRepository subjectRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    QuestionRepository questionRepository;
//    @GetMapping("/showAccount")
//    public String getListAccount(Model model, HttpSession session,
//                                 @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo)
//    {
//        Integer userId = (Integer) session.getAttribute("user_id");
//        if (userId != null) {
//            // Nếu có user_id, lấy thông tin user từ cơ sở dữ liệu
//            Account user = accountRepository.findById(userId).orElse(null);
//            Role role = roleRepository.getRoleByRoleId(user.getRoleId());
//            if (user != null) {
//                model.addAttribute("user", user);
//                model.addAttribute("role", role);
//            }
//        }
//        Page<Account> accounts = accountService.getAllAccount(pageNo);
//        model.addAttribute("accountList", accounts);
//        return "userList";
//
//    }
    @GetMapping("/showAccountList")
    public String getAccountList(Model model, HttpSession session,@Param("keyword") String keyword,
                                 @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo)
    {
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
        Page<Account> accounts = accountService.getAllAccount(pageNo);
        if (keyword!= null){
            accounts = this.accountService.searchAccountByFullName(keyword,pageNo);
            model.addAttribute("keyword",keyword);
        }
        model.addAttribute("totalPage", accounts.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("accountList", accounts);
        return "listUser";

    }
    @GetMapping("/createUser")
    public String showCreateUserPage(Model model){
        AccountDto accountDto = new AccountDto();
        model.addAttribute("accountDto", accountDto);
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(
            @Valid @ModelAttribute AccountDto accountDto,
            BindingResult result) {
        if(accountDto.getEmail().isEmpty()) {
            result.addError(new FieldError("accountDto", "email", "The email is required"));
        }
        if(accountDto.getPassword().isEmpty()) {
            result.addError(new FieldError("questionDto", "password", "You must fill password"));
        }

        if(result.hasErrors()){
            return "createUser";
        }
        MultipartFile image = accountDto.getAvatar();
        String storageFile = "";

        if(image != null && !image.isEmpty()) {
            try {
                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);
                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }
                Date createDate = new Date();
                storageFile = createDate.getTime() + "_" + image.getOriginalFilename();
                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFile),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex){
                    System.out.println("Exception: "+ex.getMessage());
                }
            } catch (Exception ex){
                System.out.println("Exception: " + ex.getMessage());
            }
        }

        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setPassword(accountDto.getPassword());
        account.setRoleId(accountDto.getRoleId());
        account.setFullName(accountDto.getFullName());
        account.setBirthDay(null);
        account.setPhone(accountDto.getPhone());
        account.setAvatar(storageFile);
        accountRepository.save(account);

        return "redirect:/account/showAccount";
    }

    @GetMapping("/editAccount")
    public String showEditUserPage(Model model,HttpSession session, @RequestParam("id") int accountId){
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
        try{
            Account account = accountRepository.findById(accountId).orElse(null);
            System.out.println(account);
            model.addAttribute("userEdit", account);
            AccountDto accountDto = new AccountDto();
            account.setEmail(accountDto.getEmail());
            account.setPassword(accountDto.getPassword());
            account.setRoleId(accountDto.getRoleId());
            account.setFullName(accountDto.getFullName());
            account.setBirthDay(null);
            account.setPhone(accountDto.getPhone());
            model.addAttribute("accountDto", accountDto);
        } catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/home/showAccountList";
        }
        return "editAccount";
    }

    @PostMapping("/editAccount")
    public String editAccount(Model model,HttpSession session,
                              @RequestParam("id") int id, @Valid @ModelAttribute AccountDto accountDto,
                              BindingResult result){
        try{
            Account account = accountRepository.findById(id).get();
            model.addAttribute(account);
            if(result.hasErrors()){
                return "/editAccount";
            }
            if(!accountDto.getAvatar().isEmpty()){
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir+account.getAvatar());
                try{
                    Files.delete(oldImagePath);
                } catch(Exception ex){
                    System.out.println("Exception: " + ex.getMessage());
                }
                MultipartFile image = accountDto.getAvatar();
                Date createDate = new Date();
                String storageFile = createDate.getTime() + "_" + image.getOriginalFilename();
                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFile),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex){
                    System.out.println("Exception: "+ex.getMessage());
                }
                account.setAvatar(storageFile);
            }
            account.setEmail(accountDto.getEmail());
            account.setPassword(accountDto.getPassword());
            account.setRoleId(accountDto.getRoleId());
            account.setFullName(accountDto.getFullName());
            account.setBirthDay(null);
            account.setPhone(accountDto.getPhone());
            accountRepository.save(account);
        } catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/home/Profile";
    }


    @GetMapping("/deleteAccount")
    public String deleteQuestion(@RequestParam("id") int id,Model model, HttpSession session) {
        accountService.deleteAccountById(id);
        return "redirect:/account/showAccountList";
    }

    @GetMapping("/searchAccount")
    public String searchQuestionByCharacter(Model model, @RequestParam("character") String character) {
        List<Account> accounts = accountService.searchAccountByName(character);
        model.addAttribute("account", accounts);
        return "redirect:/account/showAccountList";
    }
    @GetMapping("/admin")
    public String getAllAccounts(Model model){
        List<Account> accountList = accountRepository.findAll();
//        List<SubjectsEntity> subjectsList = subjectRepository.findAll();
        List<Questions> questionList = questionRepository.findAll();
        List<TopicsEntity> topicsList = topicRepository.findAll();
//        model.addAttribute("subjectList", subjectsList);
        model.addAttribute("accountList", accountList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("topicsList", topicsList);
        return "ViewAdmin";
    }
}
