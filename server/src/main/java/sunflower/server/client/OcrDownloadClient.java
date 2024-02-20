package sunflower.server.client;

import java.io.File;

public interface OcrDownloadClient {

    File download(final String pdfId);
}
