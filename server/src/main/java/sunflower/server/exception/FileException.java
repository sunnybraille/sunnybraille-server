package sunflower.server.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final ErrorCode code;
    private final String message;

    public FileException(final ErrorCode code, final Throwable cause, final String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public FileException(final ErrorCode code, final Throwable cause) {
        this(code, cause, null);
    }

    public FileException(final Throwable cause, final String message) {
        this(null, cause, message);
    }

    public FileException(final ErrorCode code) {
        this(code, null, null);
    }

    public FileException(final Throwable cause) {
        this(null, cause, null);
    }

    public FileException(final String message) {
        this(null, null, message);
    }
}
