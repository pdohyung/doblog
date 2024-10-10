package project.doblog.exception.error;

import lombok.Getter;

@Getter
public abstract class DoblogExcpetion extends RuntimeException {

    public DoblogExcpetion(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
