package project.doblog.global.exception.error;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends DoblogExcpetion {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }


    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
