package sunflower.server.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.exception.FileException;
import sunflower.server.exception.TranscriptionException;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String errorCode;

    private final String message;

    public static ExceptionResponse from(final FileException exception) {
        return new ExceptionResponse(exception.getErrorCode().name(), exception.getMessage());
    }

    public static ExceptionResponse from(final TranscriptionException exception) {
        return new ExceptionResponse(exception.getErrorCode().name(), exception.getMessage());
    }
}
