package onezerosoft.chattravel.domain;

import jakarta.persistence.*;
import lombok.*;
import onezerosoft.chattravel.domain.common.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TravelStyle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer style1; // 자연vs도시 (0~5)

    private Integer style2; // 관광vs휴식 (0~5)

    private Integer style3; // 럭셔리vs가성비 (0~5)

    private Integer style4; // 사진중요vs안중요 (0~5)

}
