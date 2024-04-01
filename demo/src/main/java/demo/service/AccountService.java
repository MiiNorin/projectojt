package demo.service;

import demo.entity.Account;
import demo.entity.AccountDto;
import demo.entity.Questions;
import demo.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

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

    public List<Account> getAccount(){
        return accountRepository.findAll();
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

    public void verify(String email, String otp, HttpSession session) {
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


    private String generateOTP(){
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email,String otp){
        String subject = "Email verification";
        String body ="your verification otp is: "+otp;
        emailService.sendEmail(email,subject,body);
    }


}
