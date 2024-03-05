package sunflower.server.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public FileException(final ErrorCode errorCode, final Throwable cause, final String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public FileException(final ErrorCode errorCode, final Throwable cause) {
        this(errorCode, cause, null);
    }

    public FileException(final Throwable cause, final String message) {
        this(null, cause, message);
    }

    public FileException(final ErrorCode errorCode) {
        this(errorCode, null, null);
    }

    public FileException(final Throwable cause) {
        this(null, cause, null);
    }

    public FileException(final String message) {
        this(null, null, message);
    }
}
