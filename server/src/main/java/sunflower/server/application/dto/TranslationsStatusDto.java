package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.client.dto.OcrProgressStatus;
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
                ocrStatus(translations.getOcrProgressStatus()),
                translations.getOcrPercentDone(),
                brailleTranslationStatus(translations.getBrailleTranslationPercentDone()),
                translations.getBrailleTranslationPercentDone()
        );
    }

    private static OcrStatus ocrStatus(final OcrProgressStatus status) {
        if (status == null) {
            return OcrStatus.NONE;
        }
        if (status == OcrProgressStatus.SPLIT) {
            return OcrStatus.PROCESSING;
        }
        return OcrStatus.COMPLETED;
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
