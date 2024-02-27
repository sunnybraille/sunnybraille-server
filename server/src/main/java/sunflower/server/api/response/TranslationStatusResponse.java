package sunflower.server.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.application.dto.TranslationStatusDto;

@Getter
@RequiredArgsConstructor
public class TranslationStatusResponse {

    @Schema(description = "Translations ID", defaultValue = "1")
    private final Long id;

    @Schema(description = "OCR 진행 상태", defaultValue = "PROCESSING",
            allowableValues = {"NONE", "PROCESSING", "COMPLETED"})
    private final String ocrStatus;

    @Schema(description = "OCR 진행도", defaultValue = "30%")
    private final Integer ocrPercentDone;

    @Schema(description = "점역 진행 상태", defaultValue = "NONE",
            allowableValues = {"NONE", "PROCESSING", "COMPLETED"})
    private final String brailleTranslationsStatus;

    @Schema(description = "점역 진행도", defaultValue = "0%")
    private final Integer brailleTranslationPercentDone;

    public static TranslationStatusResponse from(final TranslationStatusDto dto) {
        return new TranslationStatusResponse(
                dto.getId(),
                dto.getOcrStatus().name(),
                dto.getOcrPercentDone(),
                dto.getBrailleTranslationStatus().name(),
                dto.getBrailleTranslationPercentDone()
        );
    }
}
