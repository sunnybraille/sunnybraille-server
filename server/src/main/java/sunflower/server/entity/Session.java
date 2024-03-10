package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public Session(final Long memberId) {
        this.memberId = memberId;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plus(3, ChronoUnit.HOURS);
    }

    public static Session of(final Long memberId) {
        return new Session(memberId);
    }

    public boolean isValid() {
        // TODO: 현재는 만료 시간만 체크중이지만, 이후에 IP 등 다양한 검증 로직 추가
        LocalDateTime currentTime = LocalDateTime.now();
        return expiredAt.isAfter(currentTime);
    }
}