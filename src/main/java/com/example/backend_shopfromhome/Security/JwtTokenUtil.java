
package com.example.backend_shopfromhome.Security;

import com.example.backend_shopfromhome.Model.Utente;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000;

    // Genera un token JWT per l'utente
    public String generateToken(Utente user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRuolo().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Verifica il token JWT
    public boolean validateToken(String token, String email) {
        String tokenEmail = getEmailFromToken(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // Estrae l'email dal token JWT
    public String getEmailFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }

    // Verifica se il token è scaduto
    private boolean isTokenExpired(String token) {
        Date expiration = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt();

        return expiration.before(new Date());
    }
}
