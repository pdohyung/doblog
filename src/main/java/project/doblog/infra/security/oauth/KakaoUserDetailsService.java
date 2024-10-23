package project.doblog.infra.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.doblog.domain.user.User;
import project.doblog.domain.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserDetailsService extends DefaultOAuth2UserService {

    private static final String PREFIX = "낯선 ";
    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        String email = kakaoUserInfo.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.createUser(email, PREFIX + email.split("@")[0], null)
                ));

        log.info("카카오 유저 = {}", user);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(DEFAULT_ROLE);

        return new KakaoUserDetails(user.getId(), email, List.of(authority), oAuth2User.getAttributes());
    }
}
