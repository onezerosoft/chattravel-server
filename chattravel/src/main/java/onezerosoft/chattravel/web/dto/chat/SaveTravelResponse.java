package onezerosoft.chattravel.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTravelResponse {
    private Integer travelId;
    private String travelTitle;
    private LocalDateTime createdAt;
}
