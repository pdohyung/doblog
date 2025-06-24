package project.doblog.domain.user;

public enum LoginType {

    LOCAL, KAKAO;

    public static LoginType getType(String value) {
        return LoginType.valueOf(value.toUpperCase());
    }
}
