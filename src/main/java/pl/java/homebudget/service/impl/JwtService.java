package pl.java.homebudget.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private final String secret = "SECRET";
    private final int dayInMillis = 86_400_000;

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);

        return claims.getSubject();
    }

    public Date extractExpirationDate(String token) {
        Claims claims = extractClaims(token);

        return claims.getExpiration();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();

        return !isTokenExpired(token) && extractUsername(token).equals(username);
    }

    public Boolean isTokenExpired(String token) {

        return extractExpirationDate(token).before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("user", userDetails.getUsername());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + dayInMillis))
                .signWith(SignatureAlgorithm.HS256, secret)
                .addClaims(claims)
                .compact();
    }
}
