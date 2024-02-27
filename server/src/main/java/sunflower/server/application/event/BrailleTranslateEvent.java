package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BrailleTranslateEvent extends ApplicationEvent {

    private final Long id;

    public BrailleTranslateEvent(final Object source, final Long id) {
        super(source);
        this.id = id;
    }
}
