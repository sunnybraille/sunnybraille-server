package sunflower.server.application;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

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
        String requestURI = "https://api.mathpix.com/v3/pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.set("app_id", appId);
        headers.set("app_key", appKey);

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource((file).getOriginalFilename()));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("conversion_formats", Map.of("docx", true, "tex.zip", true));
        requestBody.put("math_inline_delimiters", new String[]{"\\(", "\\)"});
        requestBody.put("rm_spaces", true);

        bodyMap.add("options_json", new Gson().toJson(requestBody));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            System.out.println(responseBody);
        } else {
            System.err.println("Error occurred: " + response.getStatusCodeValue() + " - " + response.getBody());
        }

        return null;
    }
}
