package onezerosoft.chattravel.web.dto.react;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentScoreResponse {
    private Integer currentScore;
    private LocalDateTime createdAt;

}
