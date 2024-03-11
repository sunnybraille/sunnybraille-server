package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Profile("test")
public class MockTranscriptionClient implements TranscriptionClient {

    @Override
    public String transcribe(final File file) {
        return null;
    }
}
