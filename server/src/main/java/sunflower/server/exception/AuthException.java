package sunflower.server.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final String message;

    private AuthException(final Throwable cause, final String message) {
        super(cause);
        this.message = message;
    }

    public AuthException(final Throwable cause) {
        this(cause, "인증에 실패했습니다. 다시 시도해 주세요.");
    }

    public AuthException() {
        this(null);
    }
}
