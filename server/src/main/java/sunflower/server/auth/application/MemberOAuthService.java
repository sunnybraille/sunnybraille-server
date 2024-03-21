package sunflower.server.auth.application;

public interface MemberOAuthService {

    String loginURI();

    Long login(final String code);
}
