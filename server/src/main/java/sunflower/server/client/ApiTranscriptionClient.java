package sunflower.server.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
@Profile("!test")
@Component
public class ApiTranscriptionClient implements TranscriptionClient {

    private final String appURI;
    private final String keyName;
    private final RestTemplate restTemplate;

    public ApiTranscriptionClient(
            @Value("${braille-translation.uri}") String appURI,
            @Value("${braille-translation.key-name}") String keyName,
            RestTemplate restTemplate
    ) {
        this.appURI = appURI;
        this.keyName = keyName;
        this.restTemplate = restTemplate;
    }

    @Override
    public String transcribe(final File file) {
        final HttpHeaders requestHeader = createRequestHeader();
        MultiValueMap<String, Object> requestBody = createRequestBody(file);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", appURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        final ResponseEntity<String> response = restTemplate.postForEntity(appURI, requestEntity, String.class);

        log.info("Response Body: {}", response.getBody());

        return response.getBody();
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MULTIPART_FORM_DATA);
        return requestHeader;
    }

    private MultiValueMap<String, Object> createRequestBody(final File file) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(keyName, new FileSystemResource(file));
        return requestBody;
    }
}
