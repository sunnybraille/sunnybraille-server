package sunflower.server.auth.client.kakao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sunflower.server.auth.client.response.KakaoAccessTokenResponse;

@Slf4j
@Component
public class KakaoAccessTokenClient {

    private static final String GRANT_TYPE = "authorization_code";
    private final RestTemplate restTemplate;

    public KakaoAccessTokenClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KakaoAccessTokenResponse requestAccessToken(final String authTokenURI, final String restApiKey, final String redirectURI, final String code) {
        final String requestURI = authTokenURI;
        HttpHeaders requestHeader = createRequestHeader();
        final MultiValueMap<String, Object> requestBody = createRequestBody(restApiKey, redirectURI, code);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);
        log.info("Request Parameters: {}", requestBody);

        return restTemplate.postForObject(requestURI, requestEntity, KakaoAccessTokenResponse.class);
    }

    private HttpHeaders createRequestHeader() {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        return requestHeader;
    }

    private MultiValueMap<String, Object> createRequestBody(final String restApiKey, final String redirectURI, final String code) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", GRANT_TYPE);
        requestBody.add("client_id", restApiKey);
        requestBody.add("redirect_uri", redirectURI);
        requestBody.add("code", code);

        return requestBody;
    }
}
