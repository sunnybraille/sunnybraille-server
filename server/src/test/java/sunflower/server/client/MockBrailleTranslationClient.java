package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Profile("test")
public class MockBrailleTranslationClient implements BrailleTranslationClient {

    @Override
    public String translate(final File file) {
        return null;
    }
}
