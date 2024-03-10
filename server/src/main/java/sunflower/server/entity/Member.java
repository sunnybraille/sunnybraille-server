package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sunflower.server.util.PasswordUtil;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String loginId;
    private String encryptedPassword;
    private Boolean isBlind;

    public Member(final Long id, final String name, final String loginId, final String encryptedPassword, final Boolean isBlind) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.encryptedPassword = encryptedPassword;
        this.isBlind = isBlind;
    }

    public static Member of(final String loginId, final String password) {
        return new Member(null, null, loginId, PasswordUtil.encrypt(password), null);
    }

    public void checkPassword(final String password) {
        final String encryptedPassword = PasswordUtil.encrypt(password);
        if (!this.encryptedPassword.equals(encryptedPassword)) {
            throw new IllegalAccessError("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", loginId='" + loginId + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", isBlind=" + isBlind +
                '}';
    }
}
