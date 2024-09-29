package onezerosoft.chattravel.web.controller;


import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.apiPayload.ApiResponse;
import onezerosoft.chattravel.service.ChatService;
import onezerosoft.chattravel.web.dto.chat.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/create")
    public ApiResponse<ChatCreateResponse> createChat(@RequestBody ChatCreateRequest chatCreateRequest){
        ChatCreateResponse data = chatService.createChat(chatCreateRequest);
        return ApiResponse.onSuccess(data);
    }

    @GetMapping("/{chat_id}")
    public ApiResponse<TotalMessageResponse> getTotalMessage(@PathVariable(name = "chat_id") Integer chatId){
        TotalMessageResponse data = chatService.getTotalMessage(chatId);
        return ApiResponse.onSuccess(data);
    }

    @PostMapping("/{chat_id}/send")
    public ApiResponse<SendMessageResponse> sendMessage(@PathVariable(name = "chat_id") Integer chatId,
                                                        @RequestBody SendMessageRequest sendMessageRequest){
        SendMessageResponse data = chatService.sendMessage(chatId, sendMessageRequest);
        return ApiResponse.onSuccess(data);
    }

    @PostMapping("/{chat_id}/save-travel")
    public ApiResponse<SaveTravelResponse> saveTravel(@PathVariable(name = "chat_id") Integer chatId,
                                                      @RequestBody SaveTravelRequest saveTravelRequest){
        SaveTravelResponse data = chatService.saveTravel(chatId, saveTravelRequest);
        return ApiResponse.onSuccess(data);
    }

}
