package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TranscribeEvent extends ApplicationEvent {

    private final Long id;

    public TranscribeEvent(final Object source, final Long id) {
        super(source);
        this.id = id;
    }
}
