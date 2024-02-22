package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.entity.Translations;

@Getter
@RequiredArgsConstructor
public class TranslationsStatusDto {

    private final Long id;
    private final OcrStatus ocrStatus;
    private final Integer ocrPercentDone;
    private final BrailleTranslationsStatus brailleTranslationsStatus;
    private final Integer brailleTranslationPercentDone;

    public static TranslationsStatusDto from(final Translations translations) {
        return new TranslationsStatusDto(
                translations.getId(),
                ocrStatus(translations.getOcrStatus()),
                translations.getOcrPercentDone(),
                brailleTranslationStatus(translations.getTranslationPercentDone()),
                translations.getTranslationPercentDone()
        );
    }

    private static OcrStatus ocrStatus(final sunflower.server.client.dto.OcrStatus status) {
        if (status == null) {
            return TranslationsStatusDto.OcrStatus.NONE;
        }
        if (status == sunflower.server.client.dto.OcrStatus.SPLIT) {
            return TranslationsStatusDto.OcrStatus.PROCESSING;
        }
        return TranslationsStatusDto.OcrStatus.COMPLETED;
    }

    private static BrailleTranslationsStatus brailleTranslationStatus(final Integer percentDone) {
        if (percentDone == null || percentDone == 0) {
            return BrailleTranslationsStatus.NONE;
        }
        if (percentDone == 100) {
            return BrailleTranslationsStatus.COMPLETED;
        }
        return BrailleTranslationsStatus.PROCESSING;
    }

    public enum OcrStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }

    public enum BrailleTranslationsStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }
}
