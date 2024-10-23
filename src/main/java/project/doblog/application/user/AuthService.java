package project.doblog.application.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.RefreshTokenRedisRepository;
import project.doblog.domain.user.repository.UserRepository;
import project.doblog.exception.error.InvalidTokenException;
import project.doblog.exception.error.UserInfoRetrievalException;
import project.doblog.exception.error.UserNotFoundException;
import project.doblog.infra.security.jwt.token.RefreshToken;
import project.doblog.infra.security.jwt.token.TokenProvider;
import project.doblog.infra.security.jwt.token.TokenResponse;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final String DEFAULT_ROLE = "ROLE_USER";
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    @Transactional
    public TokenResponse kakaoLogin(final String code) throws JsonProcessingException {
        String token = getToken(code);
        Map<String, String> userInfo = getKakaoUserInfo(token);
        User user = saveIfNonExist(userInfo.get("email"), userInfo.get("profileImage"));

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(user.getId()), user.getEmail(), DEFAULT_ROLE);

        log.info("user = {} | New AccessToken = {}", user.getId(), tokenResponse.getAccessToken());
        log.info("user = {} | New RefreshToken = {}", user.getId(), tokenResponse.getRefreshToken());

        saveRefreshTokenOnRedis(user, tokenResponse);

        return tokenResponse;
    }

    /**
     * 로그인 성공 시 RefreshToken을 Redis에 저장
     */
    private void saveRefreshTokenOnRedis(final User user, final TokenResponse response) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(user.getId())
                .email(user.getEmail())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(DEFAULT_ROLE)))
                .refreshToken(response.getRefreshToken())
                .build());
    }

    /**
     * 카카오 AccessToken 발급
     */
    private String getToken(final String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("client_secret", kakaoClientSecret);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<String> response;
        try {
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            throw new UserInfoRetrievalException();
        }

        String responseBody = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * 카카오 사용자 정보 조회
     */
    private Map<String, String> getKakaoUserInfo(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            String responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String email = jsonNode.get("kakao_account").get("email").asText();
            String profileImage = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();

            return Map.of("email", email, "profileImage", profileImage);
        } catch (HttpClientErrorException e) {
            throw new UserInfoRetrievalException();
        }
    }

    @Transactional
    public User saveIfNonExist(String email, String profileImage) {
        return userRepository.findByEmail(email)
                .orElseGet(() ->
                        userRepository.save(
                                User.createUser(email, "낯선 " + email.split("@")[0], profileImage)
                        )
                );
    }

    /**
     * AccessToken, RefreshToken 재발급
     */
    public TokenResponse reissueAccessToken(String refreshToken) {

        log.info("Current RefreshToken = {}", refreshToken);

        // RefreshToken이 유효하지 않을 경우, 재로그인 필요
        if (tokenProvider.isValid(refreshToken)) {
            throw new InvalidTokenException();
        }

        RefreshToken findToken = Optional.ofNullable(refreshTokenRedisRepository.findByRefreshToken(refreshToken))
                .orElseThrow(InvalidTokenException::new);
        User findUser = userRepository.findByEmail(findToken.getEmail())
                .orElseThrow(UserNotFoundException::new);

        TokenResponse tokenResponse = tokenProvider.createToken(
                String.valueOf(findToken.getId()),
                findToken.getEmail(),
                findToken.getAuthority());

        saveRefreshTokenOnRedis(findUser, tokenResponse);
        SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(tokenResponse.getAccessToken()));

        log.info("user = {} | New AccessToken = {}", findUser.getId(), tokenResponse.getAccessToken());
        log.info("user = {} | New RefreshToken = {}", findUser.getId(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }
}
