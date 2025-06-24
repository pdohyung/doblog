package project.doblog.global.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DoblogExcpetion extends RuntimeException {

    public DoblogExcpetion(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();
}
