package myapp.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import myapp.service.EncryptDecript;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@UserDefinition
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Username
    public String cpf;

    @Password
    public String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Roles
    public Set<Role> roles;


    @Transactional
    public static void add(String cpf, String password, Set<Role> roles) throws Exception {
        if(roles.isEmpty()) {
            throw new Exception("User must have at least one role");
        }
        User user = new User();
        user.cpf = EncryptDecript.encrypt(cpf);
        user.password = BcryptUtil.bcryptHash(password);
        user.roles = roles;
        user.persist();
    }
}

