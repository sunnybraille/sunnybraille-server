package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OcrStatusEvent extends ApplicationEvent {

    private final Long id;
    private final String pdfId;

    public OcrStatusEvent(final Object source, final Long id, final String pdfId) {
        super(source);
        this.id = id;
        this.pdfId = pdfId;
    }
}
