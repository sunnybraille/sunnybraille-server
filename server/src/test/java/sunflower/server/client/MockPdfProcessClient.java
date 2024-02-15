package sunflower.server.client;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Component
public class MockPdfProcessClient implements PdfProcessClient {

    private static final String SECRET_KEY = "haebaragi-secret";

    @Override
    public String requestPdfId(final MultipartFile file) {
        // create random pdf id
        long expirationMs = 3600000;
        Date expiryDate = new Date(System.currentTimeMillis() + expirationMs);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
