package sunflower.server.exception;

public class FileEmptyException extends RuntimeException {

    private final int code;

    public FileEmptyException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public FileEmptyException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
