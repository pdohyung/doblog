package project.doblog.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.doblog.infra.resolver.auth.Auth;
import project.doblog.infra.resolver.refresh.RefreshToken;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @GetMapping
    public String getUser(@Auth Long userId, @RefreshToken String refreshToken) {
        System.out.println("userId = " + userId);
        System.out.println("refreshToken = " + refreshToken);
        return "user";
    }
}
