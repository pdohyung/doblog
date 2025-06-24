package project.doblog.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.doblog.global.exception.error.DoblogExcpetion;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> invalidRequestHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String firstErrorMessage = result.getFieldErrors().get(0).getDefaultMessage();
        List<String> errorList = result.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("검증 예외 리스트: {}", errorList);

        ApiResponse<Object> response = ApiResponse.of(HttpStatus.BAD_REQUEST, firstErrorMessage, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DoblogExcpetion.class)
    public ResponseEntity<ApiResponse<Object>> doblogException(DoblogExcpetion e) {
        log.warn("예외 메시지: {}", e.getMessage());

        ApiResponse<Object> response = ApiResponse.of(e.getStatus(), e.getMessage(), null);
        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
