package project.doblog.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.doblog.application.user.AuthService;
import project.doblog.infra.resolver.refresh.RefreshToken;
import project.doblog.infra.security.jwt.token.TokenResponse;
import project.doblog.utils.ServletUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 카카오 로그인 API
     */
    @PostMapping("/login/kakao")
    public ResponseEntity<Void> loginKakao(@RequestParam(name = "code") String code) throws JsonProcessingException {
        TokenResponse tokenResponse = authService.kakaoLogin(code);
        addTokensToResponse(tokenResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * JWT 토큰 재발급 API
     */
    @GetMapping("/reissue")
    public ResponseEntity<Void> reissueToken(@RefreshToken String refreshToken) {
        TokenResponse tokenResponse = authService.reissueAccessToken(refreshToken);
        addTokensToResponse(tokenResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addTokensToResponse(TokenResponse tokenResponse) {
        ServletUtils.addAuthorizationHeaderToResponse(tokenResponse.getAccessToken());
        ServletUtils.addRefreshTokenCookieToResponse(tokenResponse.getRefreshToken());
    }
}
