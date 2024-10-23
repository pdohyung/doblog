package project.doblog.exception.error;

public class InvalidTokenException extends DoblogExcpetion {

    private static final String MESSAGE = "토큰이 만료되었거나 유효하지 않습니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
