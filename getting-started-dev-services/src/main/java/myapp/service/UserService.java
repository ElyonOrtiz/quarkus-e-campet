package myapp.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import myapp.entity.Role;
import myapp.entity.User;

import java.util.Set;

@ApplicationScoped
public class UserService {

    @Transactional
    public void registerUser(String cpf, String password, Set<Role> roles) throws Exception {
        User.add(cpf, password, roles);
    }
    public User findCPF(String cpf) {
        return User.find("cpf", cpf).firstResult(); // Use named query or criteria
    }
}
