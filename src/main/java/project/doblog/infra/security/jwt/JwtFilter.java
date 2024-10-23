package project.doblog.infra.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.doblog.infra.security.jwt.token.TokenProvider;
import project.doblog.utils.ServletUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("URI = {}", request.getRequestURI());

        String bearerToken = ServletUtils.findAuthorizationHeaderToRequest();

        if (isNull(bearerToken) || !isValid(bearerToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = tokenProvider.getAuthentication(removeBearer(bearerToken));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isNull(String bearerToken) {
        return bearerToken == null;
    }

    private boolean isValid(String bearerToken) {
        return tokenProvider.isValid(bearerToken);
    }

    private String removeBearer(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }
}
