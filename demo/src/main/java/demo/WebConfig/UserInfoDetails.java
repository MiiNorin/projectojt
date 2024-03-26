package demo.WebConfig;

import demo.entity.Account;
import demo.entity.Role;
import demo.repository.AccountRepository;
import demo.repository.RoleRepository;
import demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {
    private RoleService roleService;
    private RoleRepository roleRepository;
    private String userEmail;
    private String password;
    private List<SimpleGrantedAuthority> roles;

    public UserInfoDetails(Account account, RoleService roleService) {
        String roleName = roleService.getRoleNameByRoleId(account.getRoleId());
        this.userEmail = account.getEmail();
        this.password = account.getPassword();
        this.roles = Arrays.stream(roleName.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
//        System.out.println(userEmail + password + roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
