package onezerosoft.chattravel.domain;

import jakarta.persistence.*;
import lombok.*;
import onezerosoft.chattravel.domain.common.BaseEntity;
import onezerosoft.chattravel.domain.enums.CourseType;
import onezerosoft.chattravel.domain.enums.SendType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SendType type;

    private String message;

    @Builder.Default
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courseList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
