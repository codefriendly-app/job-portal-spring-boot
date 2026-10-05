package job_portat.demo.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private UserDetailsService userDetailsService;
    public JwtService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    final private String secret = "my-first-jwt-token-authentication-2026";
   final private Key key = Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
    );

    // Generate jwt token

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .signWith(key)
                .claim("type","ADMIN")
                .expiration(new Date(System.currentTimeMillis()+1000l*60*60))
                .compact();

    }
    public String extractUser(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
