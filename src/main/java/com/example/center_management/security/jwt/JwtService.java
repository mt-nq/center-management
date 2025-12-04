package com.example.center_management.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "your-secret-key"; // TODO: đưa vào config

    // ====== ĐỌC THÔNG TIN TỪ TOKEN ======

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // ====== SINH TOKEN ======

    // 👉 Dùng username (String) vì AuthServiceImpl đang truyền user.getUsername()
    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000L * 60 * 60 * 24); // 24h

        return Jwts.builder()
                .setSubject(username)           // subject = username
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    // Nếu ở chỗ khác bạn muốn truyền luôn UserDetails:
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    // ====== VALIDATE TOKEN ======

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
