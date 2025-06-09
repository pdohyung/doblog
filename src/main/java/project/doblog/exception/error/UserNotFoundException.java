package project.doblog.exception.error;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DoblogExcpetion {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
