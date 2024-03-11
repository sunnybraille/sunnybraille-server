package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sunflower.server.entity.Transcriptions;

@Getter
public class OcrRegisterEvent extends ApplicationEvent {

    private final Transcriptions transcriptions;

    public OcrRegisterEvent(final Object source, final Transcriptions transcriptions) {
        super(source);
        this.transcriptions = transcriptions;
    }
}
