package demo.service;

import demo.entity.Account;
import demo.entity.AccountDto;
import demo.entity.Questions;
import demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service

public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Account> getAccount(){
        return accountRepository.findAll();
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }
    public Account getAccountByEmail(String email) {
        return  accountRepository.getAccountByEmail(email);
    }

    public void save(AccountDto accountDto){
        LocalDateTime Date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String creationDate = Date.format(formatter);
        Account account = new Account(0,accountDto.getRoleId(),
                accountDto.getEmail(),accountDto.getPassword(),null,
                accountDto.getFullName(),null,accountDto.getPhone(),"",
                "","",""
        );
        accountRepository.save(account);
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
}
