package myapp.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;


import java.util.Set;

@Entity
@Table(name = "person")
public class Colaborator extends PanacheEntity {

    public String name;

    @ManyToOne
    @JoinColumn(name = "position_id")
    public EmployeePosition position;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public static void add(String name, Set<User> user, EmployeePosition employeePosition) {
        Colaborator person = new Colaborator();
        person.name = name;
        person.position = employeePosition;
        person.user = (User) user;

        person.persist();
    }
}
