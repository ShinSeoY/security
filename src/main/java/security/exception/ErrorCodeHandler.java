package security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import security.exception.error.InvalidValueException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ErrorCodeHandler {

    @ExceptionHandler(InvalidValueException.class)
    private ResponseEntity<Object> handleInvalidValueException(final InvalidValueException e, final HttpServletRequest httpServletRequest) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID, httpServletRequest.getRequestURI());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        return new ResponseEntity<>(response, ErrorCode.INVALID.getHttpStatus());
    }

}
