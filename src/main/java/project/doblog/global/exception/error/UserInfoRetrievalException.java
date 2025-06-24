package project.doblog.global.exception.error;

import org.springframework.http.HttpStatus;

public class UserInfoRetrievalException extends DoblogExcpetion {

    private static final String MESSAGE = "사용자 정보를 가져오는데 실패했습니다.";

    public UserInfoRetrievalException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
