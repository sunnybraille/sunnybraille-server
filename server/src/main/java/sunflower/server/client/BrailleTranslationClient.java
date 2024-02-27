package sunflower.server.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface BrailleTranslationClient {

    File translate(File file) throws IOException;
}
