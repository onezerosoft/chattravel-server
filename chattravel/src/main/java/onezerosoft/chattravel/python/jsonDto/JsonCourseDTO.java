package onezerosoft.chattravel.python.jsonDto;

import lombok.Builder;
import onezerosoft.chattravel.web.dto.courseDTO;

import java.util.List;

@Builder
public class JsonCourseDTO {
    private String SIDO;
    private String SI;
    private String travelTitle;
    private Integer days;
    private List<courseDTO> courses;}

