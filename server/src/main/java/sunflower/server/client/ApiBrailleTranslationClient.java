package sunflower.server.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Profile("!test")
@Component
public class ApiBrailleTranslationClient implements BrailleTranslationClient {

    private final String appURI;
    private final String key;
    private final RestTemplate restTemplate;

    public ApiBrailleTranslationClient(
            @Value("${braille-translation.url}") String appURI,
            @Value("${braille-translation.key-name}") String key,
            RestTemplate restTemplate
    ) {
        this.appURI = appURI;
        this.key = key;
        this.restTemplate = restTemplate;
    }

    @Override
    public File translate(final Long id) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        return null;
    }
}
