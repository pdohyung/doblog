package project.doblog.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.doblog.exception.ApiResponse;
import project.doblog.infra.resolver.auth.Auth;
import project.doblog.infra.resolver.refresh.RefreshToken;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ApiResponse<Void> getUser(@Auth Long userId, @RefreshToken String refreshToken) {
        return ApiResponse.ok();
    }
}
