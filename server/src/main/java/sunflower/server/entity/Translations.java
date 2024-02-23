package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sunflower.server.client.dto.OcrStatus;
import sunflower.server.client.dto.OcrStatusDto;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
public class Translations {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String inputFileName;
    private String ocrPdfId;

    @Enumerated(STRING)
    private OcrStatus ocrStatus;

    private Integer ocrPercentDone;
    private Integer translationPercentDone;

    private String pdfPath;
    private String latexPath;
    private String brfPath;

    public static Translations of(final String pdfPath, final String inputFileName) {
        final Translations translations = new Translations();
        translations.start();
        translations.changePdfPath(pdfPath);
        translations.changeInputFileName(inputFileName);
        return translations;
    }

    private void start() {
        this.ocrPercentDone = 0;
        this.translationPercentDone = 0;
    }

    private void changePdfPath(final String pdfPath) {
        this.pdfPath = pdfPath;
    }

    private void changeInputFileName(final String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void startOcr() {
        this.ocrPercentDone = 0;
        this.ocrStatus = OcrStatus.SPLIT;
    }

    public void registerPdfId(final String ocrPdfId) {
        this.ocrPdfId = ocrPdfId;
    }

    public void changeOcrStatus(final OcrStatusDto dto) {
        this.ocrStatus = dto.getStatus();
        this.ocrPercentDone = dto.getPercentDone();
    }

    public void registerLatexPath(final String latexPath) {
        this.latexPath = latexPath;
    }
}
