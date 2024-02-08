package security.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import security.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class UncaughtException extends RuntimeException {

    private final ErrorCode errorCode;

    public UncaughtException() {
        super(ErrorCode.UNCAUGHT.getCode());
        this.errorCode = ErrorCode.UNCAUGHT;
    }
}
