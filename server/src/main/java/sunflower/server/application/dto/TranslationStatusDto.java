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
    private final BrailleTranslationStatus brailleTranslationStatus;
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

    private static BrailleTranslationStatus brailleTranslationStatus(final Integer percentDone) {
        if (percentDone == null || percentDone == 0) {
            return BrailleTranslationStatus.NONE;
        }
        if (percentDone == 100) {
            return BrailleTranslationStatus.COMPLETED;
        }
        return BrailleTranslationStatus.PROCESSING;
    }

    public enum OcrStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }

    public enum BrailleTranslationStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }
}
