package project.doblog.infra.security.jwt.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import project.doblog.infra.security.oauth.KakaoUserDetails;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTH_ID = "ID";
    private static final String AUTH_ROLE = "AUTHORITY";
    private static final String AUTH_EMAIL = "EMAIL";
    private Key key;
    private final String secretKey;
    private final long accessExpirations;
    private final long refreshExpirations;

    public TokenProvider(@Value("${jwt.secret_key}") String secretKey,
                         @Value("${jwt.access_expirations}") long accessExpirations,
                         @Value("${jwt.refresh_expirations}") long refreshExpirations) {
        this.secretKey = secretKey;
        this.accessExpirations = accessExpirations * 1000;
        this.refreshExpirations = refreshExpirations * 1000;
    }

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public TokenResponse createToken(String userId, String email, String role) {
        long now = (new Date()).getTime();

        Date accessValidity = new Date(now + this.accessExpirations);
        Date refreshValidity = new Date(now + this.refreshExpirations);

        String accessToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, userId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_ROLE, role))
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, userId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_ROLE, role))
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        return TokenResponse.of(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> authorities = Arrays.asList(claims.get(AUTH_ROLE)
                .toString()
                .split(","));

        List<? extends GrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        KakaoUserDetails principal = new KakaoUserDetails(Long.parseLong((String) claims.get(AUTH_ID)),
                (String) claims.get(AUTH_EMAIL),
                simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }

    public boolean isValid(String token) {
        return isBearerToken(token) && isValidToken(removeBearer(token));
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
            return false;
        }
    }

    private boolean isBearerToken(String token) {
        return token.startsWith("Bearer ");
    }

    private String removeBearer(String token) {
        return token.replace("Bearer ", "");
    }
}
