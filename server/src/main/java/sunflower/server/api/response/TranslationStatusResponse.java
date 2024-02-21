package sunflower.server.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.application.dto.TranslationsStatusDto;

@Getter
@RequiredArgsConstructor
public class TranslationStatusResponse {

    private final Long id;
    private final String ocrStatus;
    private final Integer ocrPercentDone;
    private final String brailleTranslationsStatus;
    private final Integer brailleTranslationPercentDone;

    public static TranslationStatusResponse from(final TranslationsStatusDto dto) {
        return new TranslationStatusResponse(
                dto.getId(),
                dto.getOcrStatus().name(),
                dto.getOcrPercentDone(),
                dto.getBrailleTranslationsStatus().name(),
                dto.getBrailleTranslationPercentDone()
        );
    }
}
