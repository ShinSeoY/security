package security.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import security.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {

  private final ErrorCode errorCode;

}
