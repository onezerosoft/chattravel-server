package onezerosoft.chattravel.web.controller;


import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.apiPayload.ApiResponse;
import onezerosoft.chattravel.converter.TempConverter;
import onezerosoft.chattravel.service.TempService.TempQueryService;
import onezerosoft.chattravel.web.dto.TempResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

    private final TempQueryService tempQueryService;

    @GetMapping("/test")
    public ApiResponse<TempResponse.TempTestDTO> testAPI(){

        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    @GetMapping("/exception")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag){
        tempQueryService.CheckFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }
}
