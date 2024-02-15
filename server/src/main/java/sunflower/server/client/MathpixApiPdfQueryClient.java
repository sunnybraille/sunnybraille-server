package sunflower.server.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MathpixApiPdfQueryClient {

    private static final String APP_URI = "https://api.mathpix.com/v3/pdf/";
    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public MathpixApiPdfQueryClient(
            @Value("${mathpix.app-id}") String appId,
            @Value("${mathpix.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    public Object queryPdfBy(final String pdfId) {
        final String requestURI = APP_URI + pdfId;

        HttpHeaders requestHeader = createRequestHeader();
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        final ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, requestEntity, String.class);

        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());

        return null;
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }
}
