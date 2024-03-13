package sunflower.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class MemberTranscriptionsLog {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long memberId;
    private Integer transcriptionCount;
    private LocalDate date;

    public static MemberTranscriptionsLog of(final Long memberId) {
        return new MemberTranscriptionsLog(null, memberId, 0, LocalDate.now());
    }

    public void count() {
        this.transcriptionCount++;
    }
}
