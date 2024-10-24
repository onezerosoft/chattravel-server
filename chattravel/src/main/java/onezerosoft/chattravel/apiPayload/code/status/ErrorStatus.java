package onezerosoft.chattravel.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import onezerosoft.chattravel.apiPayload.code.BaseErrorCode;
import onezerosoft.chattravel.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // api 호출 실패 응답
    MODEL_CALL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER5001","추천모델 호출에 실패하였습니다."),
    OPENAI_API_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER5002","OPENAI 응답 생성이 실패하였습니다."),

    // CHAT 관련 응답
    CHAT_API_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER5003","CHAT-API 호출에 실패하였습니다. (OPENAI 응답 없음)"),


    // TRAVEL 관련 응답

    // FEEDBACK 관련 응답
    INVALID_MESSAGE_FEEDBACK_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER5004","C_COURSE / C_TEXT 타입만 피드백 요청이 가능합니다."),



    // 테스트 응답
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}