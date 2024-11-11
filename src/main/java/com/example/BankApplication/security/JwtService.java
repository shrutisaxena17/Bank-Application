package com.example.BankApplication.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JwtService {

    private static final String SECRET_KEY = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000L;

    public String generateJwt(String customerUniqueId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("customerUniqueId", customerUniqueId);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(customerUniqueId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Claims parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String extractCustomerUniqueId(String jwt) {
        return parseJwt(jwt).get("customerUniqueId", String.class);
    }

    public String extractRole(String jwt) {
        return parseJwt(jwt).get("role", String.class);
    }

}

