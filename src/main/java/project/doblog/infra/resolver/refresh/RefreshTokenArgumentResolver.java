package project.doblog.infra.resolver.refresh;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.doblog.exception.error.InvalidTokenException;
import project.doblog.utils.CookieUtils;

import java.util.Objects;

@Slf4j
@Component
public class RefreshTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RefreshToken.class) && parameter.getParameterType()
                .equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Cookie cookie = CookieUtils.getToken(
                        Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)))
                .orElseThrow(InvalidTokenException::new);

        log.info("Cookie RefreshToken: {}", cookie.getValue());

        return cookie.getValue();
    }
}
