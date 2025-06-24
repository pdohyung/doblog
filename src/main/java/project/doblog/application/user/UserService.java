package project.doblog.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.doblog.application.user.request.UserSignUpServiceRequest;
import project.doblog.domain.user.LoginType;
import project.doblog.domain.user.Role;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpServiceRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(Role.USER)
                .loginType(LoginType.LOCAL)
                .build();

        userRepository.save(user);
    }
}
