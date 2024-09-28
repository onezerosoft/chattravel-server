package onezerosoft.chattravel.python;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonParser {

    private final ObjectMapper objectMapper;

    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * JSON 문자열을 받아 JsonNode로 변환. 내부에서 예외를 처리함.
     *
     * @param jsonString 입력받은 JSON 형식의 문자열
     * @return JsonNode로 변환된 객체, 실패 시 null 반환
     */
    public JsonNode parse(String jsonString) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 실패 시 null 반환 또는 다른 로직 처리 가능
        }
    }
}

