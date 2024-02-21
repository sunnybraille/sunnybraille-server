package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String pdfURI;
    private String ocrPdfId;
    private Double ocrPercentDone;
    private String ocrLatexFileURI;
    private String brfFileURI;

    public Translations(final String pdfURI) {
        this.pdfURI = pdfURI;
    }

    public static Translations of(final String pdfURI) {
        return new Translations(null, pdfURI, null, null, null, null);
    }
}
