package demo.service;


import demo.persistence.entity.Account;
import demo.persistence.dto.AccountDto;
import demo.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String uploadDir = "/assets/img/";

    public List<Account> getAccount(){
        return accountRepository.findAll();
    }
    public Page<Account> getAllAccount(Integer pageNo){
        Pageable pageable = PageRequest.of(pageNo - 1,4);
        return this.accountRepository.findAll(pageable);
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }
    public Account getAccountByEmail(String email) {
        return  accountRepository.getAccountByEmail(email);
    }

    public void save(AccountDto accountDto, HttpSession session){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        LocalDate Date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String creationDate = Date.format(formatter);
        Account account = new Account(4,
                accountDto.getEmail(),passwordEncoder.encode(accountDto.getPassword()),Date,
                accountDto.getFullName(),null,accountDto.getPhone(),"",
                "","",""
        );
        String otp = generateOTP();
        Account savedUser =accountRepository.save(account);
        session.setAttribute("otp",otp);
        sendVerificationEmail(savedUser.getEmail(), otp);
    }

    public boolean checkPasswordAccount(String email, String password) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account.getPassword().equals(password)) return true;
        return false;
    }

    public boolean checkAccountByEmail(String email) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) return false;
        return true;
    }
    public void deleteAccountById(int id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {

            if (account.getAvatar() != null) {
                String imagePath = "public/images/" + account.getAvatar();
                try {
                    Path path = Paths.get(imagePath);
                    Files.deleteIfExists(path);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            accountRepository.deleteById(id);
        }
    }

    public List<Account> searchAccountByName(String character) {
        return accountRepository.findAccountByFullNameContaining(character);
    }
    public Page<Account> searchAccountByFullName(String character,Integer pageNo) {
        List list = this.searchAccountByName(character);
        Pageable pageable = PageRequest.of(pageNo-1,4);

        Integer start = (int) pageable.getOffset();

        Integer end =(int) ((pageable.getOffset() +pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start,end);

        return new PageImpl<Account>(list,pageable,this.searchAccountByName(character).size());
    }


    public void verify(String email, String otp, HttpSession session) {
        Account users = accountRepository.findAccountByEmail(email);
        if (users == null){
            throw new RuntimeException("User not found");
        } else if (otp.equals(session.getAttribute("otp"))) {
//            System.out.println(users);
            accountRepository.save(users);
        }else {
            throw new RuntimeException("Internal Server error");
        }
    }
    public void verifyChangePass(String email, String otp, HttpSession session) {
        Account users = accountRepository.findAccountByEmail(email);
        if (users == null){
            throw new RuntimeException("User not found");
        } else if (otp.equals(session.getAttribute("otp"))) {
            System.out.println(users);
            accountRepository.save(users);
        }else {
            throw new RuntimeException("Internal Server error");
        }
    }

    public void updateAccount(String avatar, String fullName, String phone, String email, String school, String gender, Integer accountId) throws IOException, IOException {
        Account account = accountRepository.findById(accountId).orElse(null); // Tìm account cần cập nhật
        if (account != null) {
            if(!avatar.isEmpty()) {
                account.setAvatar(avatar);
            }
            account.setFullName(fullName);
            account.setPhone(phone);
            account.setEmail(email);
            account.setSchoolName(school); // Giả sử bạn đã có trường schoolName trong entity Account
            account.setGender(gender); // Giả sử bạn đã có trường gender trong entity Account

            // Lưu lại account sau khi đã cập nhật
            accountRepository.save(account);
        }
    }
    public String changePassword(Integer userId,String currentPass, String newPass, String renewPass) {
        if (!newPass.equals(renewPass)) {
            return "New passwords do not match!";
        }

        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            return "User not found!";
        }

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(currentPass,account.getPassword())) {
            return "Current password is incorrect!";
        }

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(newPass));
        accountRepository.save(account);

        return "Password successfully changed!";
    }
    public String changePasswordLogin(Integer userId, String newPass, String renewPass) {
        if (!newPass.equals(renewPass)) {
            return "New passwords do not match!";
        }

        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            return "User not found!";
        }

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(newPass));
        System.out.println(account.getPassword());
        accountRepository.save(account);

        return "Password successfully changed!";
    }




    public String generateOTP(){
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email,String otp){
        String subject = "Email verification";
        String body ="your verification otp is: "+otp;
        emailService.sendEmail(email,subject,body);
    }
    public void sendVerificationEmailtoChangePass(String email,String otp){
        String subject = "Email verification";
        String body ="your verification otp is: "+otp;
        emailService.sendEmail(email,subject,body);
    }


}