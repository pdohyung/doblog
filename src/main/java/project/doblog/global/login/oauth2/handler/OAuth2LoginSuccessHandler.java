package project.doblog.global.login.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.doblog.domain.user.Role;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.UserRepository;
import project.doblog.global.jwt.service.JwtService;
import project.doblog.global.login.oauth2.CustomOAuth2User;
import project.doblog.utils.ServletUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        log.info("OAuth2 로그인 성공 = {}", oAuth2User);

        User user = userRepository.findByEmail(oAuth2User.getEmail())
                .orElse(null);

        if (user == null) {
            user = userRepository.save(User.builder()
                    .email(oAuth2User.getEmail())
                    .nickname(oAuth2User.getNickname() + System.currentTimeMillis())
                    .image(oAuth2User.getImage())
                    .role(Role.USER)
                    .loginType(oAuth2User.getLoginType())
                    .socialId(oAuth2User.getSocialId())
                    .build());
        }

        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        ServletUtils.addAuthorizationHeaderToResponse(accessToken);
        ServletUtils.addAuthorizationRefreshHeaderToResponse(refreshToken);

        log.info("OAuth2Success-CustomOAuth2User= {}", oAuth2User);
        log.info("OAuth2Success-CustomOAuth2User= {}", oAuth2User);
        log.info("OAuth2Success-accessToken = {}", accessToken);
        log.info("OAuth2Success-refreshToken = {}", refreshToken);
    }
}
