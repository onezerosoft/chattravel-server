package onezerosoft.chattravel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onezerosoft.chattravel.apiPayload.code.status.ErrorStatus;
import onezerosoft.chattravel.apiPayload.exception.handler.TempHandler;
import onezerosoft.chattravel.domain.*;
import onezerosoft.chattravel.repository.*;
import onezerosoft.chattravel.web.dto.react.CurrentScoreResponse;
import onezerosoft.chattravel.web.dto.react.UserReactionRequest;
import onezerosoft.chattravel.web.dto.react.UserReactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static onezerosoft.chattravel.domain.enums.ChatStatus.COMPLETE;
import static onezerosoft.chattravel.domain.enums.SendType.C_COURSE;
import static onezerosoft.chattravel.domain.enums.SendType.C_TEXT;
import static onezerosoft.chattravel.domain.enums.UserReaction.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {
    private final CurrentScoreRepository currentScoreRepository;
    private final UserReactionRecordRepository userReactionRecordRepository;
    private final MessageRepository messageRepository;
    private final CourseChangeRecordRepository courseChangeRecordRepository;
    private final ChatRepository chatRepository;

    public UserReactionResponse saveUserReaction(Integer messageId, UserReactionRequest request){

        Optional<Message> optionalMessage = messageRepository.findById(Long.valueOf(messageId));
        if (optionalMessage.isEmpty()){
            // 오류 처리

        }
        Message message = optionalMessage.get();

        // 메세지 타입 확인 -> C_COURSE / C_TEXT 만 피드백 가능
        if (!(message.getType() == C_COURSE || message.getType() == C_TEXT)){
            throw new TempHandler(ErrorStatus.INVALID_MESSAGE_FEEDBACK_REQUEST);
        }

        // 기존 피드백 invalid 처리
        Optional<UserReactionRecord> optionalRecord = userReactionRecordRepository.findByMessageIdAndIsValid(messageId, "Y");
        if(optionalRecord.isPresent()){
            UserReactionRecord record = optionalRecord.get();
            record.setIsValid("N");
            userReactionRecordRepository.save(record);

            message.setUserReaction(null);
            messageRepository.save(message);
        }

        // 유저 피드백 저장
        if (request.getReaction().equals("P")){
            UserReactionRecord record = UserReactionRecord.builder()
                    .messageId(messageId)
                    .userReaction(POSITIVE)
                    .isValid("Y")
                    .build();
            userReactionRecordRepository.save(record);

            message.setUserReaction(POSITIVE);
            messageRepository.save(message);
        }
        if (request.getReaction().equals("N")){
            UserReactionRecord record = UserReactionRecord.builder()
                    .messageId(messageId)
                    .userReaction(NEGATIVE)
                    .isValid("Y")
                    .build();
            userReactionRecordRepository.save(record);

            message.setUserReaction(NEGATIVE);
            messageRepository.save(message);
        }

        // 전체 유저 피드백 기반으로 현재 스코어 계산
        CurrentScore currentScore = calculateCurrentScore();

        return UserReactionResponse.builder()
                .currentScore(currentScore.getAccuracy())
                .createdAt(currentScore.getCreatedAt())
                .build();
    }

    public CurrentScoreResponse getCurrentScore(){
        CurrentScore currentScore = calculateCurrentScore();
        return CurrentScoreResponse.builder()
                .currentScore(currentScore.getAccuracy())
                .createdAt(currentScore.getCreatedAt())
                .build();
    }

    public CourseChangeRecord saveChangeCourseRequest(Long requestMessageId, Long responseMessageId, String userMessage){
        CourseChangeRecord record = CourseChangeRecord.builder()
                .requestMessageId(requestMessageId)
                .responseMessageId(responseMessageId)
                .userMessage(userMessage)
                .build();
        record = courseChangeRecordRepository.save(record);

        return record;
    }

    private CurrentScore calculateCurrentScore(){

        // 코스 순서 변경 요청 없이 생성 완료
        int courseNotChangeCount = getCourseNotChangeCount();

        // 코스 순서 변경 요청 있음
        int courseChangeCount = (int) courseChangeRecordRepository.count();

        float positiveFeedback = (int) userReactionRecordRepository.countByIsValidAndUserReaction("Y",POSITIVE);

        int negativeFeedback = (int) userReactionRecordRepository.countByIsValidAndUserReaction("Y",NEGATIVE);


        if (courseChangeCount + positiveFeedback + negativeFeedback == 0){
            return CurrentScore.builder()
                    .accuracy(0)
                    .ReactionCount(0)
                    .CourseChangeCount(0)
                    .build();
        }

        // 정확도 계산 로직
        int accuracy = (int)((positiveFeedback + courseNotChangeCount) / (courseChangeCount + courseNotChangeCount + positiveFeedback + negativeFeedback) * 100);

        CurrentScore score = CurrentScore.builder()
                .accuracy(accuracy)
                .ReactionCount((int)positiveFeedback + negativeFeedback)
                .CourseChangeCount(courseChangeCount)
                .build();
        currentScoreRepository.save(score);

        return score;
    }

    private int getCourseNotChangeCount(){

        int courseNotChangeCount = 0;

        // ChatStatus가 'COMPLETE'인 ChatList
        List<Chat> completeChatList = chatRepository.findByStatus(COMPLETE);

        for (Chat chat : completeChatList){
            List<Message> messageList = messageRepository.findByChatAndType(chat, C_COURSE);
            if (messageList.size() == 1){
                courseNotChangeCount += 1;
            }
        }

        return courseNotChangeCount;
    }
}
