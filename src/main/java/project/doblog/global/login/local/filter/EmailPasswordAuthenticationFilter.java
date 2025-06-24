package project.doblog.global.login.local.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class EmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_LOGIN_REQUEST = new AntPathRequestMatcher("/login", "POST");

    private final ObjectMapper objectMapper;

    public EmailPasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_REQUEST);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("EmailPasswordAuthenticationFilter - 로그인 검증 시작");
        EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(emailPassword.getEmail(), emailPassword.getPassword());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Getter
    private static class EmailPassword {
        private String email;
        private String password;
    }
}
