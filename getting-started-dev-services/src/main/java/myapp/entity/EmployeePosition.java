package myapp.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "employee_position")
public class EmployeePosition extends PanacheEntity {

    public String positionName;

}
