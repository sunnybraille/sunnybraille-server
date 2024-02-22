package sunflower.server.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Profile("!test")
@Component
public class ApiOcrDownloadClient implements OcrDownloadClient {

    private static final String APP_URI = "https://api.mathpix.com/v3/pdf/%s.tex";

    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public ApiOcrDownloadClient(
            @Value("${mathpix.app-id}") String appId,
            @Value("${mathpix.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public File download(final String pdfId) {
        final String requestURI = String.format(APP_URI, pdfId);

        HttpHeaders requestHeader = createRequestHeader();
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                requestURI,
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );

        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());

        final File file = new File("src/main/latex/" + pdfId + ".zip");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(response.getBody());
        } catch (IOException e) {
            log.error("[ERROR] Error occurred while saving file: {}", e.getMessage());
            throw new RuntimeException("파일을 읽는데 실패함!");
        }

        return file;
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }
}
