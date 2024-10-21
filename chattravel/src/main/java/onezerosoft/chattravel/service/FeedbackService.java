package onezerosoft.chattravel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onezerosoft.chattravel.web.dto.react.CurrentScoreResponse;
import onezerosoft.chattravel.web.dto.react.UserReactionRequest;
import onezerosoft.chattravel.web.dto.react.UserReactionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {

    public UserReactionResponse saveUserReaction(Integer messageId, UserReactionRequest request){
        return UserReactionResponse.builder()
                //.currentScore(80)
                //.createdAt(LocalDateTime.now())
                .build();
    }

    public CurrentScoreResponse getCurrentScore(){
        return CurrentScoreResponse.builder()
                //.currentScore(80)
                //.createdAt(LocalDateTime.now())
                .build();
    }
}
