package org.example.user.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.example.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your_secret_key"; // Nên lưu trong application.properties
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername()) // Username làm subject
                .withClaim("userId", user.getId()) // Thêm userId vào token
                .withClaim("roles", user.getRole().toString()) // Thêm role vào token
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Hết hạn sau 1 giờ
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public Long extractUserId(String token) {
        return decodeToken(token).getClaim("userId").asLong(); // Lấy userId từ token
    }

    public String extractRoles(String token) {
        return decodeToken(token).getClaim("roles").asString(); // Lấy roles từ token
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return userDetails.getUsername().equals(extractUsername(token));
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}