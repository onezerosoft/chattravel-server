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
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer day;    // 몇일차인지

    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;


    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Place> placeList = new ArrayList<>();

}
