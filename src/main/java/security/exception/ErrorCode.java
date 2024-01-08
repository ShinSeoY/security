package security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
  INVALID("M001", "invalid", HttpStatus.BAD_REQUEST),
  NOT_FOUND("M002", "not-found", HttpStatus.NOT_FOUND),
  LOGIN_NOT_MATCHED("M003", "not-found", HttpStatus.NOT_FOUND);
//  UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED),
//  UNCAUGHT("uncaught-error", HttpStatus.INTERNAL_SERVER_ERROR),
//
//  DUPLICATION("duplicated", HttpStatus.BAD_REQUEST),
//  ALREADY_DELETED("already-deleted", HttpStatus.BAD_REQUEST),
//  NOT_FOUND_ROLE("not-found-role", HttpStatus.BAD_REQUEST),
//  DUPLICATION_NICK("duplication-nick", HttpStatus.OK),
//  DUPLICATION_USER("duplication-user", HttpStatus.BAD_REQUEST),
//  NO_MATCH_PASSWORD("no-match-password", HttpStatus.BAD_REQUEST),
//  SAME_PASSWORD("same-password", HttpStatus.BAD_REQUEST);

  private final String code;

  private final String message;

  private final HttpStatus httpStatus;

}
