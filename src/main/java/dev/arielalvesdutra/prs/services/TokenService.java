package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private final Long EXPIRATION = 86400000L;

    private final String SECRET = "K7BdEpBiB8ZQ9hKipojPREG2H3GhNAo3";

    public String generateToken(Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        return generateToken(loggedUser);
    }

    public String generateToken(User user) {

        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + EXPIRATION);

        return Jwts.builder()
                .setIssuer("Post Register System")
                .setSubject(user.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public Long getUserId(String token) {

        Claims claims = Jwts.parser().
                setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
