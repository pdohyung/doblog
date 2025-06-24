package project.doblog.global.login.oauth2.userinfo;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getNickName();

    public abstract String getImageUrl();
}
