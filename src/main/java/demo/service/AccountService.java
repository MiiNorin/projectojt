package demo.service;

import demo.entity.Account;
import demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deleteAccountById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            // Xóa ảnh đại diện nếu có
            if (account.getAvatar() != null) {
                String imagePath = "public/images/avatars/" + account.getAvatar();
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

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
}