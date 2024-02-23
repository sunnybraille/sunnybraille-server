package sunflower.server.client;

public interface OcrDownloadClient {

    byte[] download(final String pdfId);
}
