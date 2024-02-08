package security.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import security.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class InvalidTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN.getCode());
        this.errorCode = ErrorCode.INVALID_TOKEN;
    }
}
