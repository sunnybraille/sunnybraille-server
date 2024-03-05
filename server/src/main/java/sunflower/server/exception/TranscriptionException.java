package sunflower.server.exception;

import lombok.Getter;

@Getter
public class TranscriptionException extends RuntimeException {

    private final ErrorCode errorCode;

    public TranscriptionException(final String message, final String errorCode, final Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.from(errorCode);
    }

    public TranscriptionException(final String errorCode) {
        this(ErrorCode.from(errorCode).getMessage(), errorCode, null);
    }

    public TranscriptionException(final String message, final String errorCode) {
        this(message, errorCode, null);
    }
}
