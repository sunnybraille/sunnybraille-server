package sunflower.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpHeaders.LOCATION;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String HTTP_METHODS = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("*")
                .allowedMethods(HTTP_METHODS.split(","))
                .exposedHeaders(LOCATION);
    }
}
