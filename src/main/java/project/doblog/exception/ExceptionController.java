package project.doblog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.doblog.exception.error.DoblogExcpetion;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String firstErrorMessage = result.getFieldErrors().get(0).getDefaultMessage();
        List<String> errorList = result.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("검증 예외 리스트: {}", errorList);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message(firstErrorMessage)
                        .build());
    }

    @ExceptionHandler(DoblogExcpetion.class)
    public ResponseEntity<ErrorResponse> doblogException(DoblogExcpetion e) {
        log.warn("예외 메시지: {}", e.getMessage());

        int statusCode = e.getStatusCode();

        return ResponseEntity.status(statusCode)
                .body(ErrorResponse.builder()
                        .code(String.valueOf(statusCode))
                        .message(e.getMessage())
                        .build());
    }
}
