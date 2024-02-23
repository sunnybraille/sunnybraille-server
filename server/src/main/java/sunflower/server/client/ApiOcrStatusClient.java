package sunflower.server.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import sunflower.server.client.dto.OcrStatusDto;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Profile("!test")
@Component
public class ApiOcrStatusClient implements OcrStatusClient {

    private static final String COMPLETED_STATUS = "completed";
    private static final long POLLING_INTERVAL_MS = 1000L; // 1 sec
    private static final Duration MAX_WAIT_DURATION = Duration.ofSeconds(20);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String appURI;
    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public ApiOcrStatusClient(
            @Value("${ocr.status-uri}") String appURI,
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
    public OcrStatusDto checkStatus(final String pdfId) {
        final String requestURI = appURI + pdfId;

        HttpHeaders requestHeader = createRequestHeader();
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        final ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, requestEntity, String.class);

        final String responseBody = response.getBody();
        log.info("Response Body: {}", responseBody);

        try {
            return objectMapper.readValue(responseBody, OcrStatusDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }

    @Deprecated
    @Override
    public boolean isDone(final String pdfId) {
        final String requestURI = appURI + pdfId;

        HttpHeaders requestHeader = createRequestHeader();
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        final ResponseEntity<String> response = fetchResponse(requestURI, requestEntity);

        log.info("Response Status Code: {}", response.getStatusCode());
        log.info("Response Body: {}", response.getBody());

        return true;
    }

    @Deprecated
    private ResponseEntity<String> fetchResponse(final String requestURI, final HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        final Instant startTime = Instant.now();
        while (true) {
            final ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, requestEntity, String.class);
            final String responseBody = response.getBody();

            if (responseBody.contains(COMPLETED_STATUS)) {
                log.info("OCR Conversion Completed! Response Body: {}", responseBody);
                return response;
            }

            log.info("OCR Conversion Not Ready! Response Body: {}", responseBody);

            try {
                Thread.sleep(POLLING_INTERVAL_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            final Instant endTime = Instant.now();
            final Duration timeGap = Duration.between(startTime, endTime);
            if (timeGap.compareTo(MAX_WAIT_DURATION) >= 0) {
                log.error("OCR File Fetch Failed! 소요 시간: {}", timeGap);
                throw new RuntimeException("[ERROR] 시간 초과");
            }
        }
    }
}
