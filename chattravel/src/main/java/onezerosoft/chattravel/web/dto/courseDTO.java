package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import java.util.List;

@Builder
public class courseDTO {
    private Integer courseId;
    private Integer day;
    private List<placeDTO> places;
}
