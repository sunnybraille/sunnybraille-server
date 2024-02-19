package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
public class Translations {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String mathpixPdfId;
    private Double ocrPercentDone;
    private String ocrLatexFileURI;
    private String brfFileURI;

    public Translations(final String mathpixPdfId, final Double ocrPercentDone, final String ocrLatexFileURI, final String brfFileURI) {
        this.mathpixPdfId = mathpixPdfId;
        this.ocrPercentDone = ocrPercentDone;
        this.ocrLatexFileURI = ocrLatexFileURI;
        this.brfFileURI = brfFileURI;
    }
}
