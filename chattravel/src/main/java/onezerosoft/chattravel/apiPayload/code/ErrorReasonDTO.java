package onezerosoft.chattravel.apiPayload.code;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReasonDTO {

    private HttpStatus httpStatus;
    private String code;
    private String message;
    private boolean isSuccess;

}