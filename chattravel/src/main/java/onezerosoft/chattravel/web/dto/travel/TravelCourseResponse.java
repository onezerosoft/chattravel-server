package onezerosoft.chattravel.web.dto.travel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onezerosoft.chattravel.web.dto.courseDTO;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelCourseResponse {
    private String SIDO;
    private String SI;
    private String travelTitle;
    private Integer days;
    private List<courseDTO> courses;

}
