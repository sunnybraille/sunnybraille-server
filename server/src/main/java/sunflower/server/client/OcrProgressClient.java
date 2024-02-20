package sunflower.server.client;

import sunflower.server.client.dto.OcrProgressDto;

public interface OcrProgressClient {

    OcrProgressDto progress(String pdfId);

    boolean isDone(String pdfId);
}

