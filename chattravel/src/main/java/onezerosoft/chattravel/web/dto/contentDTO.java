package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import onezerosoft.chattravel.web.dto.courseDTO;

import java.util.List;

@Builder
@Getter
@Setter
public class contentDTO {
    private String message;
    private List<courseDTO> courses;
}