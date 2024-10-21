package onezerosoft.chattravel.web.controller;


import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.apiPayload.ApiResponse;
import onezerosoft.chattravel.service.FeedbackService;
import onezerosoft.chattravel.web.dto.react.CurrentScoreResponse;
import onezerosoft.chattravel.web.dto.react.UserReactionRequest;
import onezerosoft.chattravel.web.dto.react.UserReactionResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/{message_id}")
    public ApiResponse<UserReactionResponse> saveUserReaction(@PathVariable(name = "message_id") Integer messageId,
                                                              @RequestBody UserReactionRequest userReactionRequest){
        UserReactionResponse data = feedbackService.saveUserReaction(messageId, userReactionRequest);
        return ApiResponse.onSuccess(data);
    }

    @GetMapping("/currentScore")
    public ApiResponse<CurrentScoreResponse> getCurrentScore(){
        CurrentScoreResponse data = feedbackService.getCurrentScore();
        return ApiResponse.onSuccess(data);
    }
}
