package project.doblog.api.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import project.doblog.application.user.request.UserSignUpServiceRequest;

@Getter
public class UserSignUpRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private final String password;

    @NotBlank(message = "닉네임을 입력하세요.")
    private final String nickname;

    @Builder
    public UserSignUpRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public UserSignUpServiceRequest toServiceRequest() {
        return UserSignUpServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
