package sunflower.server.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sunflower.server.exception.AuthException;
import sunflower.server.exception.FileException;
import sunflower.server.exception.TranscriptionException;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    private String message;

    public static ExceptionResponse from(final FileException exception) {
        return new ExceptionResponse(exception.getErrorCode().name(), exception.getMessage());
    }

    public static ExceptionResponse from(final TranscriptionException exception) {
        return new ExceptionResponse(exception.getErrorCode().name(), exception.getMessage());
    }

    public static ExceptionResponse from(final AuthException exception) {
        return new ExceptionResponse(null, exception.getMessage());
    }
}
