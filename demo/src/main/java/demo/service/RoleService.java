package demo.service;

import demo.entity.Account;
import demo.entity.Role;
import demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Role getRoleById(int id) {
        return  roleRepository.getRoleByRoleId(id);
    }
    public String getRoleNameByRoleId(int id) {
        Role role = getRoleById(id);
        if (role != null) {
            return role.getRoleName();
        } else {
            // Handle the case where no role with the given id is found
            throw new RuntimeException("Role not found with ID: " + id);
        }
    }
}
