package myapp.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import myapp.entity.Role;


@ApplicationScoped
public class RoleService {

    @PersistenceContext
    EntityManager entityManager;

    public Role findRoleByName(String roleName) {
        // Método para encontrar uma role pelo nome
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult();
    }
}
