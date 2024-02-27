package sunflower.server.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
@Profile("!test")
@Component
public class ApiOcrRegisterClient implements OcrRegisterClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String appURI;
    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public ApiOcrRegisterClient(
            @Value("${ocr.register-uri}") String appURI,
            @Value("${ocr.app-id}") String appId,
            @Value("${ocr.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appURI = appURI;
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    @Deprecated
    @Override
    public String requestPdfId(final MultipartFile file) {
        final HttpHeaders requestHeader = createRequestHeader();
        final MultiValueMap<String, Object> requestBody = createRequestBody(file);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", appURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        final ResponseEntity<String> response = restTemplate.postForEntity(appURI, requestEntity, String.class);

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

    @Deprecated
    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MULTIPART_FORM_DATA);
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);
        return requestHeader;
    }

    @Deprecated
    private MultiValueMap<String, Object> createRequestBody(final MultipartFile file) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", file.getResource());

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("conversion_formats", Map.of("docx", true, "tex.zip", true));
        bodyMap.put("math_inline_delimiters", Arrays.asList("$", "$"));
        bodyMap.put("rm_spaces", true);
        String optionsJson = convertToJson(bodyMap);

        requestBody.add("options_json", optionsJson);
        return requestBody;
    }

    private String convertToJson(final Map<String, Object> body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String requestPdfId(final File file) {
        final HttpHeaders requestHeader = createRequestHeader();
        final MultiValueMap<String, Object> requestBody = createRequestBody(file);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", appURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        final ResponseEntity<String> response = restTemplate.postForEntity(appURI, requestEntity, String.class);

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

    private MultiValueMap<String, Object> createRequestBody(final File file) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", new FileSystemResource(file));

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("conversion_formats", Map.of("docx", true, "tex.zip", true));
        bodyMap.put("math_inline_delimiters", Arrays.asList("$", "$"));
        bodyMap.put("rm_spaces", true);
        String optionsJson = convertToJson(bodyMap);
        requestBody.add("options_json", optionsJson);

        return requestBody;
    }
}
