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

    private String fileName;
    private String pdfURI;
    private String ocrPdfId;

    @Enumerated(STRING)
    private OcrStatus ocrStatus;

    private Integer ocrPercentDone;
    private String ocrLatexFileURI;
    private Integer translationPercentDone;
    private String brfFileURI;

    public static Translations of(final String pdfURI) {
        final Translations translations = new Translations();
        translations.changePdfURI(pdfURI);
        return translations;
    }

    private void changePdfURI(final String pdfURI) {
        this.pdfURI = pdfURI;
    }

    public void startOcr() {
        this.ocrPercentDone = 0;
        this.ocrStatus = OcrStatus.SPLIT;
    }

    public void registerPdfId(final String pdfId) {
        this.ocrPdfId = pdfId;
    }

    public void changeOcrStatus(final OcrStatusDto dto) {
        this.ocrStatus = dto.getStatus();
        this.ocrPercentDone = dto.getPercentDone();
    }
}
