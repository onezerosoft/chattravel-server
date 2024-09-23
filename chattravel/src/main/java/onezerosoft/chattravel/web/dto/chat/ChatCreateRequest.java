package onezerosoft.chattravel.web.dto.chat;

import lombok.*;
import onezerosoft.chattravel.web.dto.regionDTO;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatCreateRequest {
    private regionDTO region;
    private Integer days;
    private List<Integer> styleList;
}