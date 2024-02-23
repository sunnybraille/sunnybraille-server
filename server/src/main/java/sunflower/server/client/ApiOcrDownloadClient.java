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

@Slf4j
@Profile("!test")
@Component
public class ApiOcrDownloadClient implements OcrDownloadClient {

    private final String appURI;
    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public ApiOcrDownloadClient(
            @Value("${ocr.download-uri}") String appURI,
            @Value("${ocr.app-id}") String appId,
            @Value("${ocr.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appURI = appURI;
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public byte[] download(final String pdfId) {
        final String requestURI = String.format(appURI, pdfId);

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

        return response.getBody();
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }
}
