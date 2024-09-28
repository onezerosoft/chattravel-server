package onezerosoft.chattravel.domain;

import jakarta.persistence.*;
import lombok.*;
import onezerosoft.chattravel.domain.common.BaseEntity;
import onezerosoft.chattravel.domain.enums.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String loginId;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private SocialType loginType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private TravelStyle travelStyle;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Travel> travelList = new ArrayList<>();;


}
