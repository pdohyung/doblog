package project.doblog.application.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpServiceRequest {

    private final String email;
    private final String password;
    private final String nickname;

    @Builder
    public UserSignUpServiceRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
