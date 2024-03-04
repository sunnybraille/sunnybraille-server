package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sunflower.server.util.PasswordUtil;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String loginId;
    private String encryptedPassword;
    private Boolean isBlind;

    public void checkPassword(final String password) {
        final String encryptedPassword = PasswordUtil.encrypt(password);
        if (!this.encryptedPassword.equals(encryptedPassword)) {
            throw new IllegalAccessError("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }
}
