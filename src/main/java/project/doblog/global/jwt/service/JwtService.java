package project.doblog.global.jwt.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.doblog.global.config.JwtConfig;
import project.doblog.domain.user.RefreshToken;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.RefreshTokenRedisRepository;
import project.doblog.domain.user.repository.UserRepository;

import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String AUTH_EMAIL = "EMAIL";

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public Optional<User> findUser(String token) {
        return userRepository.findByEmail(extractEmail(removeBearer(token)));
    }

    public RefreshToken findRefreshToken(String refreshToken) {
        if (isNotNull(refreshToken)) {
            return refreshTokenRedisRepository.findByRefreshToken(removeBearer(refreshToken))
                    .orElse(null);
        }
        return null;
    }

    public String createAccessToken(User user) { //todo. 토큰 정보에 user pk
        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(AUTH_EMAIL, user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessExpirations()))
                .signWith(jwtConfig.getKey())
                .compact();
    }

    public String createRefreshToken(User user) {
        String refreshToken = Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(AUTH_EMAIL, user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpirations()))
                .signWith(jwtConfig.getKey())
                .compact();

        refreshTokenRedisRepository.deleteById(user.getId());
        saveRefreshTokenOnRedis(user.getId(), refreshToken);
        return refreshToken;
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(AUTH_EMAIL, String.class);
    }

    public boolean isValid(String token) {
        return isNotNull(token) && isBearerToken(token) && isValidToken(removeBearer(token));
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtConfig.getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("JwtService-JwtException = {}", e.getMessage());
        }

        return false;
    }

    private boolean isNotNull(String token) {
        return token != null;
    }

    private boolean isBearerToken(String token) {
        return token.startsWith("Bearer ");
    }

    private String removeBearer(String token) {
        return token.replace("Bearer ", "");
    }

    public void saveRefreshTokenOnRedis(Long id, String refreshToken) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(id)
                .refreshToken(refreshToken)
                .build());
    }
}
