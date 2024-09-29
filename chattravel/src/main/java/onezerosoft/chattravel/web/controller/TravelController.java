package onezerosoft.chattravel.web.controller;


import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.apiPayload.ApiResponse;
import onezerosoft.chattravel.service.ChatService;
import onezerosoft.chattravel.service.TravelService;
import onezerosoft.chattravel.web.dto.chat.SendMessageResponse;
import onezerosoft.chattravel.web.dto.travel.TravelCourseRequest;
import onezerosoft.chattravel.web.dto.travel.TravelCourseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/travel")
@RequiredArgsConstructor
public class TravelController {
    private final TravelService travelService;

    @GetMapping("/{travel_id}")
    public ApiResponse<TravelCourseResponse> getTravelCourse(@PathVariable(name = "travel_id") String travelId){
        TravelCourseResponse data = travelService.getTravelCourse(travelId);
        return ApiResponse.onSuccess(data);
    }
}
