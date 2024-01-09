package security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
  INVALID("M001", "invalid", HttpStatus.BAD_REQUEST),
  NOT_FOUND("M002", "not-found", HttpStatus.NOT_FOUND),
  LOGIN_NOT_MATCHED("M003", "login-not-matched", HttpStatus.NOT_FOUND);

  private final String code;

  private final String message;

  private final HttpStatus httpStatus;

}
