package onezerosoft.chattravel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onezerosoft.chattravel.domain.CurrentScore;
import onezerosoft.chattravel.repository.CurrentScoreRepository;
import onezerosoft.chattravel.repository.UserReactionRecordRepository;
import onezerosoft.chattravel.web.dto.react.CurrentScoreResponse;
import onezerosoft.chattravel.web.dto.react.UserReactionRequest;
import onezerosoft.chattravel.web.dto.react.UserReactionResponse;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {
    private final CurrentScoreRepository currentScoreRepository;

    public UserReactionResponse saveUserReaction(Integer messageId, UserReactionRequest request){
        return UserReactionResponse.builder()
                //.currentScore(80)
                //.createdAt(LocalDateTime.now())
                .build();
    }

    public CurrentScoreResponse getCurrentScore(){
        CurrentScore currentScore = currentScoreRepository.findLatestCreated();
        return CurrentScoreResponse.builder()
                .currentScore(currentScore.getAccuracy())
                .createdAt(currentScore.getCreatedAt())
                .build();
    }
}
