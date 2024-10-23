package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import onezerosoft.chattravel.domain.enums.UserReaction;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class messageDTO {
    private Integer messageId;
    private String messageType;
    private contentDTO content;
    private UserReaction reaction;
    private LocalDateTime createdAt;

}