package demo.WebConfig;

import demo.entity.Account;
import demo.repository.AccountRepository;
import demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Configuration
public class OurUserDetailsService implements UserDetailsService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final RoleService roleService;


    @Autowired
    public OurUserDetailsService(AccountRepository accountRepository, RoleService roleService) {
        this.accountRepository = accountRepository;
        this.roleService = roleService; // Khởi tạo roleService
    }
    @Override
    public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByEmail(user_name);
        return account.map(Account -> new UserInfoDetails(Account, roleService)).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

    }
}
