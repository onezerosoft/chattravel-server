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
public class Travel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String sido;    //시도

    private String si;      //시

    private String travelTitle;

    private Integer days;

    @Enumerated(EnumType.STRING)
    private TravelStatus status;

    private String image;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Course> courseList = new ArrayList<>();

}
