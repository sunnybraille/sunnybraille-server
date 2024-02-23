package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TranslateEvent extends ApplicationEvent {

    private final Long id;

    public TranslateEvent(final Object source, final Long id) {
        super(source);
        this.id = id;
    }
}
