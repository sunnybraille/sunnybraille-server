package sunflower.server.client;

import sunflower.server.client.dto.OcrStatusDto;

public interface OcrStatusClient {

    OcrStatusDto checkStatus(String pdfId);

    boolean isDone(String pdfId);
}

