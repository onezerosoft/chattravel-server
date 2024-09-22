package onezerosoft.chattravel.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReasonDTO {

    private HttpStatus httpStatus;
    private String code;
    private String message;
    private boolean isSuccess;

}