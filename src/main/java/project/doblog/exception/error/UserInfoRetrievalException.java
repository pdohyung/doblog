package project.doblog.exception.error;

public class UserInfoRetrievalException extends DoblogExcpetion {

    private static final String MESSAGE = "사용자 정보를 가져오는데 실패했습니다.";

    public UserInfoRetrievalException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
