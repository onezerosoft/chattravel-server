package onezerosoft.chattravel.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onezerosoft.chattravel.web.dto.messageDTO;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalMessageResponse {
    public Integer chatId;
    public String chatname;
    public Integer totalMessageCount;
    public LocalDateTime createdAt;
    public List<messageDTO> messages;
}
