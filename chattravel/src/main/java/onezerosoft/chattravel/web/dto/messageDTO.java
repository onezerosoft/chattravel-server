package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import onezerosoft.chattravel.web.dto.courseDTO;

import java.util.List;
import java.time.LocalDateTime;

@Builder
public class messageDTO {
    private Integer messageId;
    private String type;
    private contentDTO content;
    private LocalDateTime createdAt;

    @Builder
    private class contentDTO {
        private String message;
        private List<courseDTO> courses;
    }
}