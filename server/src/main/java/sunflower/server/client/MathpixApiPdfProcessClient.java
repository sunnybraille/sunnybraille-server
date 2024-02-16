package sunflower.server.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
@Profile("!test")
@Component
public class MathpixApiPdfProcessClient implements PdfProcessClient {

    private static final String APP_URI = "https://api.mathpix.com/v3/pdf";
    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public MathpixApiPdfProcessClient(
            @Value("${mathpix.app-id}") String appId,
            @Value("${mathpix.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    public String requestPdfId(final MultipartFile file) {
        final ObjectMapper objectMapper = new ObjectMapper();

        final HttpHeaders requestHeader = createRequestHeader();
        final MultiValueMap<String, Object> requestBody = createRequestBody(file, objectMapper);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", APP_URI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        final ResponseEntity<String> response = restTemplate.postForEntity(APP_URI, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("OCR 과정에서 문제가 발생했습니다. Mathpix API 에러 메세지: {}", response.getBody());
            throw new IllegalArgumentException("OCR 과정에서 문제가 발생했습니다.");
        }

        try {
            final JsonNode root = objectMapper.readTree(response.getBody());
            final String pdfID = root.get("pdf_id").asText();
            log.info("PDF ID: {}", pdfID);
            return pdfID;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MULTIPART_FORM_DATA);
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }

    private MultiValueMap<String, Object> createRequestBody(
            final MultipartFile file,
            final ObjectMapper objectMapper
    ) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", file.getResource());

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("conversion_formats", Map.of("docx", true, "tex.zip", true));
        bodyMap.put("math_inline_delimiters", Arrays.asList("$", "$"));
        bodyMap.put("rm_spaces", true);
        String optionsJson = convertToJson(objectMapper, bodyMap);

        requestBody.add("options_json", optionsJson);
        return requestBody;
    }

    private String convertToJson(
            final ObjectMapper objectMapper,
            final Map<String, Object> body
    ) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
