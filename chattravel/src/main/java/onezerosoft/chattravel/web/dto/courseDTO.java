package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class courseDTO {
    private Integer courseId;
    private Integer day;
    private List<placeDTO> places;
}
