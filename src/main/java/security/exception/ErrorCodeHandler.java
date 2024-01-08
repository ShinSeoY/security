package security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import security.exception.error.InvalidValueException;
import security.exception.error.UserException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ErrorCodeHandler {


  // 개별 exception 설정할 때 사용
  @ExceptionHandler(InvalidValueException.class)
  private ResponseEntity<Object> handleInvalidValueException(final InvalidValueException e, final HttpServletRequest httpServletRequest) {
    ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID, httpServletRequest.getRequestURI());
    for(StackTraceElement element: e.getStackTrace()){
      log.error(element.toString());
    }
    return new ResponseEntity<>(response, ErrorCode.INVALID.getHttpStatus());
  }

  // 한번에 error 설정할 때 사용
  @ExceptionHandler(UserException.class)
  private ResponseEntity<ErrorCode> handleUserException(final UserException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.error("handleUserException... {}", errorCode);
    return new ResponseEntity<>(errorCode, errorCode.getHttpStatus());
  }

}
