package onezerosoft.chattravel.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onezerosoft.chattravel.apiPayload.code.BaseCode;
import onezerosoft.chattravel.apiPayload.code.ReasonDTO;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공하였습니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "생성하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}