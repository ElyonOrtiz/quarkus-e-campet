package myapp.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped // Defina o escopo da classe para que seja gerenciada pelo CDI
public class JwtTokenGenerator {

    public String generateToken(String username, Set<String> roles) {

        // Gera o token JWT
        return Jwt.issuer("auth-jwt")
                .upn(username)
                .groups(roles)
                .expiresIn(36000)
                .sign();
    }
}
