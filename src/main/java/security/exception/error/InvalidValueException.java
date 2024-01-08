package security.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import security.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class InvalidValueException extends RuntimeException {

  private final ErrorCode errorCode;

  public InvalidValueException(){
    super(ErrorCode.INVALID.getCode());
    this.errorCode = ErrorCode.INVALID;
  }
}
