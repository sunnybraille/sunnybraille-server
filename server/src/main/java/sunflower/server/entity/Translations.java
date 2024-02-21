package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sunflower.server.client.dto.OcrProgressStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
public class Translations {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String pdfURI;
    private String ocrPdfId;

    @Enumerated(STRING)
    private OcrProgressStatus ocrProgressStatus;

    private Integer ocrPercentDone;
    private String ocrLatexFileURI;
    private String brfFileURI;
    private Integer brailleTranslationPercentDone;

    public static Translations of(final String pdfURI) {
        final Translations translations = new Translations();
        translations.setPdfURI(pdfURI);
        return translations;
    }
}
