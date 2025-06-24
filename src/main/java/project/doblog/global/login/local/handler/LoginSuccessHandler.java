package project.doblog.global.login.local.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.UserRepository;
import project.doblog.global.exception.error.UserNotFoundException;
import project.doblog.global.jwt.service.JwtService;
import project.doblog.utils.ServletUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("LoginSuccessHandler - 로그인 성공");
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        ServletUtils.addAuthorizationHeaderToResponse(accessToken);
        ServletUtils.addAuthorizationRefreshHeaderToResponse(refreshToken);
    }
}
