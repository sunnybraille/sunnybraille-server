package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.entity.Translations;

@Getter
@RequiredArgsConstructor
public class TranscriptionStatusDto {

    private final Long id;
    private final OcrStatus ocrStatus;
    private final Integer ocrPercentDone;
    private final TranscriptionStatus transcriptionStatus;
    private final Integer transcriptionPercentDone;

    public static TranscriptionStatusDto from(final Translations translations) {
        return new TranscriptionStatusDto(
                translations.getId(),
                ocrStatus(translations.getOcrStatus()),
                translations.getOcrPercentDone(),
                brailleTranslationStatus(translations.getTranslationPercentDone()),
                translations.getTranslationPercentDone()
        );
    }

    private static OcrStatus ocrStatus(final sunflower.server.client.dto.OcrStatus status) {
        if (status == null) {
            return TranscriptionStatusDto.OcrStatus.NONE;
        }
        if (status == sunflower.server.client.dto.OcrStatus.SPLIT) {
            return TranscriptionStatusDto.OcrStatus.PROCESSING;
        }
        return TranscriptionStatusDto.OcrStatus.COMPLETED;
    }

    private static TranscriptionStatus brailleTranslationStatus(final Integer percentDone) {
        if (percentDone == null || percentDone == 0) {
            return TranscriptionStatus.NONE;
        }
        if (percentDone == 100) {
            return TranscriptionStatus.COMPLETED;
        }
        return TranscriptionStatus.PROCESSING;
    }

    public enum OcrStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }

    public enum TranscriptionStatus {

        NONE,
        PROCESSING,
        COMPLETED,
        ;
    }
}
