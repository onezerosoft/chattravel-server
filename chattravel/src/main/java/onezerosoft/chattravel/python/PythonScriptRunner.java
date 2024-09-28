package onezerosoft.chattravel.python;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

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
}
