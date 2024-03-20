package sunflower.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sunflower.server.util.PasswordUtil;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String nickname;

    @Column(unique = true)
    private String loginId;
    private String encryptedPassword;

    @Enumerated(STRING)
    private LoginType loginType;
    private Long oauthId;
    private Boolean isBlind;

    private Member(
            final Long id,
            final String nickname,
            final String loginId,
            final String encryptedPassword,
            final LoginType loginType,
            final Long oauthId,
            final Boolean isBlind
    ) {
        this.id = id;
        this.nickname = nickname;
        this.loginId = loginId;
        this.encryptedPassword = encryptedPassword;
        this.loginType = loginType;
        this.oauthId = oauthId;
        this.isBlind = isBlind;
    }

    @Builder(builderMethodName = "basicLogin")
    public Member(
            final String nickname,
            final String loginId,
            final String password
    ) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.encryptedPassword = encryptPassword(password);
    }

    private static String encryptPassword(final String password) {
        if (password != null) {
            return PasswordUtil.encrypt(password);
        }
        return null;
    }

    @Builder(builderMethodName = "oauth")
    public Member(
            final String nickname,
            final LoginType loginType,
            final Long oauthId
    ) {
        this.nickname = nickname;
        this.loginType = loginType;
        this.oauthId = oauthId;
    }

    public void checkPassword(final String password) {
        final String encryptedPassword = PasswordUtil.encrypt(password);
        if (!this.encryptedPassword.equals(encryptedPassword)) {
            throw new IllegalAccessError("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }
}
