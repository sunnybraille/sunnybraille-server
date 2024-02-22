package sunflower.server.application.event;

import org.springframework.context.ApplicationEvent;
import sunflower.server.application.OcrStatusEventListener;

public class OcrDownloadEvent extends ApplicationEvent {

    private final Long id;
    private final String pdfId;

    public OcrDownloadEvent(final Object source, final Long id, final String pdfId) {
        super(source);
        this.id = id;
        this.pdfId = pdfId;
    }
}
