package sunflower.server.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final int code;

    public FileException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public FileException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
