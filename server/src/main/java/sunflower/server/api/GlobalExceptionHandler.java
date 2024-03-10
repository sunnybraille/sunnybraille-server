package sunflower.server.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sunflower.server.api.response.ExceptionResponse;
import sunflower.server.exception.AuthException;
import sunflower.server.exception.FileException;
import sunflower.server.exception.TranscriptionException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(final BadRequestException exception) {
        log.warn("BadRequestException | " + exception.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(exception.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ExceptionResponse> handleFileException(final FileException exception) {
        log.error("file exception 발생");
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(ExceptionResponse.from(exception));
    }

    @ExceptionHandler(TranscriptionException.class)
    public ResponseEntity<ExceptionResponse> handleTranscriptionException(final TranscriptionException exception) {
        log.error("점역 과정에서 예외 발생");
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(ExceptionResponse.from(exception));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleAuthException(final AuthException exception) {
        log.error("점역 과정에서 예외 발생");
        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(ExceptionResponse.from(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception exception) {
        log.error("", exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(exception.getMessage());
    }
}
