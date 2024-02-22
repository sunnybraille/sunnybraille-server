package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.entity.Translations;

@Getter
@RequiredArgsConstructor
public class TranslationStatusDto {

    private final Long id;
    private final OcrStatus ocrStatus;
    private final Integer ocrPercentDone;
    private final BrailleTranslationsStatus brailleTranslationsStatus;
    private final Integer brailleTranslationPercentDone;

    public static TranslationStatusDto from(final Translations translations) {
        return new TranslationStatusDto(
                translations.getId(),
                ocrStatus(translations.getOcrStatus()),
                translations.getOcrPercentDone(),
                brailleTranslationStatus(translations.getTranslationPercentDone()),
                translations.getTranslationPercentDone()
        );
    }

    private static OcrStatus ocrStatus(final sunflower.server.client.dto.OcrStatus status) {
        if (status == null) {
            return TranslationStatusDto.OcrStatus.NONE;
        }
        if (status == sunflower.server.client.dto.OcrStatus.SPLIT) {
            return TranslationStatusDto.OcrStatus.PROCESSING;
        }
        return TranslationStatusDto.OcrStatus.COMPLETED;
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
