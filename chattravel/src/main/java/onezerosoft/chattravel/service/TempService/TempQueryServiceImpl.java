package onezerosoft.chattravel.service.TempService;

import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.apiPayload.code.status.ErrorStatus;
import onezerosoft.chattravel.apiPayload.exception.handler.TempHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService{

    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}
