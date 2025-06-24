package project.doblog.global.login.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) return null;

        return String.valueOf(account.get("email"));
    }

    @Override
    public String getNickName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) return null;

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) return null;

        return String.valueOf(profile.get("nickname"));
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) return null;

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) return null;

        return String.valueOf(profile.get("thumbnail_image_url"));
    }
}
