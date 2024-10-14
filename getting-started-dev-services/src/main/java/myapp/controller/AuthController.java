package myapp.controller;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import myapp.entity.Role;
import myapp.entity.User;
import myapp.security.JwtTokenGenerator;
import myapp.service.EncryptDecript;
import myapp.service.RoleService;
import myapp.service.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/auth")
@Consumes("application/json")
@Produces("application/json")
@PermitAll
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @Inject
    JwtTokenGenerator tokenGenerator;

    @POST
    @Path("/register")
    @Transactional
    public Response register(UserDTO userDTO) {
        try {
            String cpfEncrypted = EncryptDecript.encrypt(userDTO.cpf);

            // Encontre as roles passadas no DTO e adicione ao Set de roles do usuário
            Set<Role> roles = userDTO.roles.stream()
                    .map(roleName -> roleService.findRoleByName(roleName)) // Método para buscar a role no banco
                    .collect(Collectors.toSet());

            if (userService.findCPF(cpfEncrypted) != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("CPF already registered")
                        .build();
            }
            userService.registerUser(userDTO.cpf, userDTO.password, roles);
            return Response.ok(cpfEncrypted).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error registering user")
                    .build();
        }
    }


    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        try {
            String cpfEncrypted = EncryptDecript.encrypt(loginDTO.cpf);
            User user = User.find("cpf", cpfEncrypted).firstResult();
            if (user != null && BcryptUtil.matches(loginDTO.password, user.password)){
                Set<String> roleNames = user.roles.stream()
                        .map(Role::getRoleName) // Converte cada Role para o seu nome (String)
                        .collect(Collectors.toSet());
                String token = tokenGenerator.generateToken(user.cpf, roleNames);
                return Response.ok(user.roles).header("Authorization", token).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error logging in user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error logging in user")
                    .build();
        }
    }
}