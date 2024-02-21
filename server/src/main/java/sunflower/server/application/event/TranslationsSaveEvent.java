package sunflower.server.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sunflower.server.entity.Translations;

@Getter
public class TranslationsSaveEvent extends ApplicationEvent {

    private final Translations translations;

    public TranslationsSaveEvent(final Object source, final Translations translations) {
        super(source);
        this.translations = translations;
    }
}
