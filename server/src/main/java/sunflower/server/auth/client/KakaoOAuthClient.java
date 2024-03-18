package sunflower.server.auth.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sunflower.server.auth.client.response.KakaoOAuthResponse;

import java.util.Map;

@Slf4j
@Component
public class KakaoOAuthClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GRANT_TYPE = "authorization_code";
    private final RestTemplate restTemplate;

    public KakaoOAuthClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void login(final String authTokenURI, final String restApiKey, final String redirectURI, final String code) {
        final String requestURI = authTokenURI;
        HttpHeaders requestHeader = createRequestHeader();
        final MultiValueMap<String, Object> requestBody = createRequestBody(restApiKey, redirectURI, code);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        final KakaoOAuthResponse response = restTemplate.postForObject(requestURI, requestEntity, KakaoOAuthResponse.class);
    }

    private MultiValueMap<String, Object> createRequestBody(final String restApiKey, final String redirectURI, final String code) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", GRANT_TYPE);
        requestBody.add("client_id", restApiKey);
        requestBody.add("redirect_uri", redirectURI);
        requestBody.add("code", code);

        return requestBody;
    }

    private String convertToJson(final Map<String, Object> body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("Content-Type", "application/x-www-form-urlencoded");
        return requestHeader;
    }
}
