package sunflower.server.client;

import java.io.File;
import java.io.IOException;

public interface BrailleTranslationClient {

    String translate(File file) throws IOException;
}
