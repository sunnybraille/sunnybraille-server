package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OcrDownloadEvent extends ApplicationEvent {

    private final Long id;

    public OcrDownloadEvent(final Object source, final Long id) {
        super(source);
        this.id = id;
    }
}
