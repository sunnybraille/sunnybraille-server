package sunflower.server.application;

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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
@Service
@Profile("!test")
public class PdfTranslationService {

    private final String appId;
    private final String appKey;
    private final RestTemplate restTemplate;

    public PdfTranslationService(
            @Value("${mathpix.app-id}") String appId,
            @Value("${mathpix.app-key}") String appKey,
            RestTemplate restTemplate
    ) {
        this.appId = appId;
        this.appKey = appKey;
        this.restTemplate = restTemplate;
    }

    public Long translate(final MultipartFile file) {
        log.info(file.getOriginalFilename());
        log.info("변환을 시작합니다.");

        String requestURI = "https://api.mathpix.com/v3/pdf";

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MULTIPART_FORM_DATA);
        requestHeader.set("app_id", appId);
        requestHeader.set("app_key", appKey);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", file.getResource());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("conversion_formats", Map.of("docx", true, "tex.zip", true));
        bodyMap.put("math_inline_delimiters", Arrays.asList("$", "$"));
        bodyMap.put("rm_spaces", true);
        String optionsJson = null;

        try {
            optionsJson = objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        requestBody.add("options_json", optionsJson);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        ResponseEntity<String> response = restTemplate.postForEntity(requestURI, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                final String pdfID = root.get("pdf_id").asText();
                log.info("PDF ID: " + pdfID);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
