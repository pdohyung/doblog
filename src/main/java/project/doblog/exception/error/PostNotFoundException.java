package project.doblog.exception.error;

public class PostNotFoundException extends DoblogExcpetion{

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
