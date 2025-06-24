package project.doblog.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.doblog.api.user.request.UserSignUpRequest;
import project.doblog.application.user.UserService;
import project.doblog.global.exception.ApiResponse;
import project.doblog.global.resolver.auth.Auth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<Void> signUp(@RequestBody UserSignUpRequest request) {
        userService.signUp(request.toServiceRequest());
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<String> getUser(@Auth String email) {
        return ApiResponse.ok(email);
    }
}
