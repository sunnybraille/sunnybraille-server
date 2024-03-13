package sunflower.server.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {

    P("PDF 파일 처리에 문제가 발생했습니다.."),
    O("OCR API 호출에서 문제가 발생했습니다."),
    L("Latex 파일 처리에 문제가 발생했습니다."),
    T("점역 API 호출에서 문제가 발생했습니다."),
    B("BRF 파일 처리에 문제가 발생했습니다."),
    C("일일 가능한 점역 횟수를 초과했습니다."),
    ;

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public static ErrorCode from(final String errorCode) {
        return Arrays.stream(ErrorCode.values())
                .filter(it -> it.name().equals(errorCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 에러 코드는 존재하지 않습니다."));
    }
}
