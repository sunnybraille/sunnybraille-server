package sunflower.server.client.dto;

import java.util.Arrays;

public enum OcrStatus {

    LOADED,
    SPLIT,
    COMPLETED,
    ;

    public static OcrStatus from(final String status) {
        return Arrays.stream(OcrStatus.values())
                .filter(it -> it.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OcrStatus의 내용과 매핑할 수 없는 상태임!"));
    }
}
