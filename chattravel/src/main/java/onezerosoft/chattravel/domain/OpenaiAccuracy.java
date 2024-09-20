package onezerosoft.chattravel.domain;

import jakarta.persistence.*;
import lombok.*;
import onezerosoft.chattravel.domain.common.BaseEntity;

@Entity
@Getter
@Setter
public class OpenaiAccuracy extends BaseEntity {
    @Id
    private Long id = 1L;

    private Integer accuracy;
    private Integer ResponseCount; // 총 유저평가 횟수

}
