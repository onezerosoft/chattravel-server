package onezerosoft.chattravel.python;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class PythonScriptRunner {

    // 인자를 받는 메서드 추가
    public String runScript(String scriptPath, List<String> args) {
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder(); // 오류 출력 저장
        try {
            // 명령어 리스트에 Python 스크립트 경로와 인자 추가
            List<String> command = new ArrayList<>();
            command.add("C:/Users/user/anaconda3/python.exe");  // Python 실행 명령
            command.add(scriptPath); // Python 스크립트 경로

            // 인자 리스트 추가
            if (args != null) {
                command.addAll(args);  // 인자를 추가
            }

            // ProcessBuilder 설정
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Python 스크립트 실행 후 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 오류 출력 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorOutput.append(errorLine).append("\n");
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Error running Python script. Exit code: " + exitCode);
                log.error("Python Script Error Output: " + errorOutput.toString());
                throw new RuntimeException("Error running Python script");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return output.toString();
    }

    // 파이썬 스크립트 호출 테스트
    public void runScriptTest(){
        // 테스트 할 파이썬 스크립트 경로와 인자
        // String scriptPath = "chattravel-recommend/src/prediction.py";  // 실제 경로로 수정 필요
        // List<String> scriptArgs = Arrays.asList("guest4", "수도권", "서울시", "3", "1,2,3,4");

        String scriptPath = "chattravel-recommend/src/openai/course_api.py";  // 실제 경로로 수정 필요
        List<String> scriptArgs = Arrays.asList("2", "서울역사박물관, 오브젝트 성수점, 한전아트센터, 창덕궁", "콘래드", "서울시");

        // 스크립트 실행 테스트
        String result = runScript(scriptPath, scriptArgs);
    }

    // json 로드 테스트
    public void jsonLoadTest(){
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath = "chattravel-recommend/src/result/prediction_result.json";
        JsonNode jsonNode = null;

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            jsonNode = objectMapper.readTree(reader);
        } catch (Exception e) {
            e.printStackTrace();  // 파일이 없거나 JSON 형식이 잘못되었을 경우 예외 처리
        }

        JsonNode places = jsonNode.get("place");
        JsonNode accommodations = jsonNode.get("accommodation");

        String place = "";
        if (places != null && places.isArray()) {
            place = StreamSupport.stream(places.spliterator(), false)
                    .map(p -> p.get("Item").asText())
                    .collect(Collectors.joining(", "));
        }
        String accomodation = "";
        if (accommodations != null && accommodations.isArray()) {
            accomodation = StreamSupport.stream(accommodations.spliterator(), false)
                    .map(a -> a.get("Item").asText())
                    .collect(Collectors.joining(", "));
        }

        System.out.println(place);
        System.out.println(accomodation);

    }

    public static void main(String[] args) {
        PythonScriptRunner runner = new PythonScriptRunner();

        runner.runScriptTest();
        //runner.jsonLoadTest();
    }
}
