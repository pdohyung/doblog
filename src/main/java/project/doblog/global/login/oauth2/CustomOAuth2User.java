package project.doblog.global.login.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import project.doblog.domain.user.LoginType;
import project.doblog.domain.user.Role;
import project.doblog.global.login.oauth2.userinfo.OAuth2UserInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String email;
    private final String nickname;
    private final String image;
    private final LoginType loginType;
    private final String socialId;

    @Builder
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String email, String nickname, String image, LoginType loginType, String socialId) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.nickname = nickname;
        this.image = image;
        this.loginType = loginType;
        this.socialId = socialId;
    }

    public static CustomOAuth2User of(OAuthAttributes oAuthAttributes) {
        OAuth2UserInfo userInfo = oAuthAttributes.getUserInfo();
        return CustomOAuth2User.builder()
                .authorities(Collections.singleton(new SimpleGrantedAuthority(Role.USER.getKey())))
                .attributes(userInfo.getAttributes())
                .nameAttributeKey(oAuthAttributes.getKey())
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickName())
                .image(userInfo.getImageUrl())
                .loginType(oAuthAttributes.getLoginType())
                .socialId(userInfo.getId())
                .build();
    }
}
