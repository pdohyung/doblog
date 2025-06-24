package project.doblog.global.login.oauth2;

import lombok.Builder;
import lombok.Getter;
import project.doblog.domain.user.LoginType;
import project.doblog.global.login.oauth2.userinfo.KakaoOAuth2UserInfo;
import project.doblog.global.login.oauth2.userinfo.OAuth2UserInfo;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String key;
    private LoginType loginType;
    private OAuth2UserInfo userInfo;

    @Builder
    public OAuthAttributes(String key, LoginType loginType, OAuth2UserInfo userInfo) {
        this.key = key;
        this.loginType = loginType;
        this.userInfo = userInfo;
    }

    public static OAuthAttributes of(LoginType type, String key, Map<String, Object> attributes) {
//        if (type.equals(SocialType.KAKAO)) {
//            return ofKakao(key, attributes);
//        }
        return ofKakao(key, attributes);
    }

    public static OAuthAttributes ofKakao(String key, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .key(key)
                .loginType(LoginType.KAKAO)
                .userInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }
}
