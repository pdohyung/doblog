package project.doblog.global.login.local.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.doblog.domain.user.LoginType;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));


        if (!user.getLoginType().equals(LoginType.LOCAL)) {
            throw new UsernameNotFoundException("소셜 로그인 계정입니다.");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        log.info("LoginService-UserDetails = {}", userDetails);

        return userDetails;
    }
}
