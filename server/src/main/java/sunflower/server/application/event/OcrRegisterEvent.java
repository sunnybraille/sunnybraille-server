package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sunflower.server.entity.Translations;

@Getter
public class OcrRegisterEvent extends ApplicationEvent {

    private final Translations translations;

    public OcrRegisterEvent(final Object source, final Translations translations) {
        super(source);
        this.translations = translations;
    }
}
