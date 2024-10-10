package project.doblog.infra.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.doblog.exception.ErrorResponse;
import project.doblog.utils.ServletUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String accessToken = ServletUtils.findAuthorizationHeaderToRequest();

        log.info("JwtAuthenticationFailEntryPoint | accessToken: {}", accessToken);
        log.warn("JwtAuthenticationFailEntryPoint | 인증되지 않은 요청입니다.");

        String json = objectMapper.writeValueAsString(ErrorResponse.builder()
                .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .message("인증되지 않은 사용자 입니다.")
                .build());

        setResponseProperties(response);
        writeJsonToResponse(response, json);
    }

    private void setResponseProperties(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void writeJsonToResponse(HttpServletResponse response, String json) throws IOException {
        response.getWriter().write(json);
    }
}
