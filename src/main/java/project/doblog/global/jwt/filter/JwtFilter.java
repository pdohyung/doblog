package project.doblog.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import project.doblog.domain.user.RefreshToken;
import project.doblog.domain.user.User;
import project.doblog.global.exception.ApiResponse;
import project.doblog.global.jwt.service.JwtService;
import project.doblog.utils.ServletUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> WHITE_LIST = List.of("/error", "/favicon.ico", "/login", "/oauth2", "/h2-console", "/users/sign-up");

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info("JwtFilter-URL = {}", path);

        if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // todo. refreshToken 쿠키 저장
        String accessToken = ServletUtils.findAuthorizationHeaderToRequest();
        String refreshToken = ServletUtils.findAuthorizationRefreshHeaderToRequest();
        RefreshToken findRefreshToken = jwtService.findRefreshToken(refreshToken);
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);
        log.info("findRefreshToken = {}", findRefreshToken);

        if (jwtService.isValid(refreshToken) && findRefreshToken != null) {
            if (jwtService.isValid(accessToken)) {
                jwtService.findUser(accessToken)
                        .ifPresent(this::savaAuthentication);
                log.info("JwtFilter-유효한 AccessToken 유저 = {}", jwtService.findUser(accessToken));
                filterChain.doFilter(request, response);
                return;
            }

            jwtService.findUser(refreshToken)
                    .ifPresent(user -> {
                        String newAccessToken = jwtService.createAccessToken(user);
                        String newRefreshToken = jwtService.createRefreshToken(user);
                        log.info("JwtFilter-newAccessToken = {}", newAccessToken);
                        log.info("JwtFilter-newRefreshToken = {}", newRefreshToken);
                        ServletUtils.addAuthorizationHeaderToResponse(newAccessToken);
                        ServletUtils.addAuthorizationRefreshHeaderToResponse(newRefreshToken);
                        savaAuthentication(user);
                    });
            log.info("JwtFilter-유효한 RefreshToken 유저 = {}", jwtService.findUser(refreshToken));
            filterChain.doFilter(request, response);
            return;
        }


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("인증되지 않은 사용자입니다.")
                        .build()
        ));
    }

    private void savaAuthentication(User user) {
        String password = user.getPassword();

        if (password == null) {
            password = UUID.randomUUID().toString(); // OAuth2 회원에 대한 처리
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()); // todo. credentials, 권한 처리

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("JwtFilter-SecurityContextHolder = {}", SecurityContextHolder.getContext().getAuthentication());
    }
}
