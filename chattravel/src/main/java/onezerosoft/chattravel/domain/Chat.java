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
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long userId;

    private String chatname;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String sido;    //시도

    private String si;      //시

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();


}
