package onezerosoft.chattravel.domain;

import jakarta.persistence.*;
import lombok.*;
import onezerosoft.chattravel.domain.common.BaseEntity;

@Entity
@Getter
@Setter
public class OpenaiAccuracy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer accuracy;
    private Integer ResponseCount; // 총 유저 평가 횟수

}
