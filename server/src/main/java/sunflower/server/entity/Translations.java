package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;

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
    private File ocrLatexFile;
    private File brfFile;

    public Translations(final String mathpixPdfId, final Double ocrPercentDone, final File ocrLatexFile, final File brfFile) {
        this.mathpixPdfId = mathpixPdfId;
        this.ocrPercentDone = ocrPercentDone;
        this.ocrLatexFile = ocrLatexFile;
        this.brfFile = brfFile;
    }
}
