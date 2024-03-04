package sunflower.server.api.response;

import lombok.Getter;

@Getter
public class PdfRegisterResponse {

    private String originalFileName;

    public PdfRegisterResponse() {
    }

    public PdfRegisterResponse(final String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public static PdfRegisterResponse from(final String originalFilename) {
        return new PdfRegisterResponse(originalFilename);
    }
}
