package onezerosoft.chattravel.apiPayload.exception.handler;

import onezerosoft.chattravel.apiPayload.code.BaseErrorCode;
import onezerosoft.chattravel.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}