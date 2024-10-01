package onezerosoft.chattravel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onezerosoft.chattravel.apiPayload.code.status.ErrorStatus;
import onezerosoft.chattravel.apiPayload.exception.handler.TempHandler;
import onezerosoft.chattravel.converter.ChatConverter;
import onezerosoft.chattravel.domain.*;
import onezerosoft.chattravel.domain.enums.CourseType;
import onezerosoft.chattravel.python.PythonScriptRunner;
import onezerosoft.chattravel.repository.*;
import onezerosoft.chattravel.web.dto.chat.*;
import onezerosoft.chattravel.web.dto.courseDTO;
import onezerosoft.chattravel.web.dto.placeDTO;
import onezerosoft.chattravel.web.dto.regionDTO;
import onezerosoft.chattravel.web.dto.travel.TravelCourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static onezerosoft.chattravel.domain.enums.ChatStatus.COMPLETE;
import static onezerosoft.chattravel.domain.enums.ChatStatus.CREATE;
import static onezerosoft.chattravel.domain.enums.SendType.*;
import static onezerosoft.chattravel.domain.enums.CourseType.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final TravelStyleRepository travelStyleRepository;
    private final MessageRepository messageRepository;
    private final TravelRepository travelRepository;
    private final CourseRepository courseRepository;
    private final ChatConverter chatConverter;
    private static final String base_path = "/home/ubuntu/chattravel-server/chattravel-recommend/src/";
    //private static final String base_path =  "/onezerosoft/chattravel-server/chattravel-recommend/src/"; 인텔리제이 테스트 실행시

    @Autowired
    private PythonScriptRunner pythonScriptRunner;


    public ChatCreateResponse createChat(ChatCreateRequest request) {
        regionDTO region = request.getRegion();
        Integer days = request.getDays();
        List<Integer> styleList = request.getStyleList();

        // 1. 유저 여행스타일 - 더미데이터 생성
        TravelStyle style = TravelStyle.builder()
                .style1(styleList.get(0))
                .style2(styleList.get(1))
                .style3(styleList.get(2))
                .style4(styleList.get(3))
                .build();
        style = travelStyleRepository.save(style);

        Long styleId = style.getId();
        String userId = "guest"+styleId.toString();

        log.info("1. 유저 아이디(여행 스타일) 생성 완료: "+userId);

        // 2. 여행 코스 생성 (추천 모델 + OpenAI API 호출)
        // 여행지&숙박지 추천 모델 - prediction.py 스크립트 실행

        String scriptPath = base_path+"prediction.py";
        String si = String.join(",", region.si);
        String travelStyle = styleList.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<String> scriptArgs = Arrays.asList(userId, region.sido, si, days.toString(), travelStyle);

        pythonScriptRunner.runScript(scriptPath, scriptArgs);

        ObjectMapper objectMapper = new ObjectMapper();

        String predict_result_file = base_path+String.format("result/prediction_result_%s.json",userId);
        JsonNode predictApiJson = null;

        File f1 = new File(predict_result_file);
        try (FileInputStream fis = new FileInputStream(f1);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            predictApiJson = objectMapper.readTree(reader);
        } catch (Exception e) {
            log.error("추천 모델 응답 실패 - prediction.py 응답 없음");
            e.printStackTrace();  // 파일이 없거나 JSON 형식이 잘못되었을 경우 예외 처리
            throw new TempHandler(ErrorStatus.MODEL_CALL_FAIL);

        }

//        JsonNode places = predictApiJson.get("place");
//        JsonNode accommodations = predictApiJson.get("accommodation");
//
//        String place = "";
//        if (places != null && places.isArray()) {
//            place = StreamSupport.stream(places.spliterator(), false)
//                    .map(p -> p.get("Item").asText())
//                    .collect(Collectors.joining(", "));
//        }
//        String accomodation = "";
//        if (accommodations != null && accommodations.isArray()) {
//            accomodation = StreamSupport.stream(accommodations.spliterator(), false)
//                    .map(a -> a.get("Item").asText())
//                    .collect(Collectors.joining(", "));
//        }
        log.info("2. 추천 여행지&숙소 생성 완료");
        log.info(predictApiJson.toPrettyString());

        // LLM 코스 생성 - course_api.py 스크립트 실행
        scriptPath = base_path+"openai/course_api.py";
        scriptArgs = Arrays.asList(days.toString(), region.sido, userId);

        pythonScriptRunner.runScript(scriptPath, scriptArgs);

        String course_result_file = base_path+String.format("result/course_api_result_%s.json", userId);
        JsonNode courseApiJson = null;

        File f2 = new File(course_result_file);
        try (FileInputStream fis = new FileInputStream(f2);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            courseApiJson = objectMapper.readTree(reader);
        } catch (Exception e) {
            log.error("LLM 코스 생성 실패 - course_api.py 응답 없음");
            e.printStackTrace();  // 파일이 없거나 JSON 형식이 잘못되었을 경우 예외 처리
            throw new TempHandler(ErrorStatus.OPENAI_API_FAIL);
        }  finally {
            // 파일 삭제 시도
            if (f2.exists()) {
                boolean isDeleted = f2.delete();
                if (isDeleted) {
                    log.info("파일이 성공적으로 삭제되었습니다: " + f2.getPath());
                } else {
                    log.warn("파일 삭제에 실패했습니다: " + f2.getPath());
                }
            }
        }
        // 남은 파일 삭제 시도
        if (f1.exists()) {
            boolean isDeleted = f1.delete();
            if (isDeleted) {
                log.info("파일이 성공적으로 삭제되었습니다: " + f1.getPath());
            } else {
                log.warn("파일 삭제에 실패했습니다: " + f1.getPath());
            }
        }

        log.info("3. LLM 코스 생성 완료");
        System.out.println(courseApiJson.toPrettyString());

        // 3. 채팅 및 메세지 생성
        Chat chat = Chat.builder()
                .username(userId)
                .sido(region.sido)
                .status(CREATE)
                .chatname(courseApiJson.get("courseTitle").asText())
                .build();
        chat = chatRepository.save(chat);
        Integer chatId = chat.getId().intValue();

        Message message = Message.builder()
                .type(C_COURSE)
                .chat(chat)
                .build();

        List<Course> courseList = createCourseList(days,courseApiJson,message);
        message.setCourseList(courseList);
        messageRepository.save(message);

        ChatCreateResponse response = chatConverter.toChatCreateResponse(chatId);
        return response;

    }

    public TotalMessageResponse getTotalMessage(Integer chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(Long.valueOf(chatId));
        if (chatOptional.isEmpty()){
            //예외처리
        }
        Chat chat = chatOptional.get();
        List<Message> totalMessages = chat.getMessageList().stream()
                .sorted(Comparator.comparing(Message::getId))
                .collect(Collectors.toList());

        TotalMessageResponse response = chatConverter.toTotalMessageResponse(chat, totalMessages);
        return response;
    }

    public SendMessageResponse sendMessage(Integer chatId, SendMessageRequest request) {
        String userMessage = request.getUserMessage();
        String currentCourse = "";


        Optional<Chat> chatOptional = chatRepository.findById(Long.valueOf(chatId));
        if (chatOptional.isEmpty()){
            //예외처리
        }
        Chat chat = chatOptional.get();

        // 유저 메세지 저장
        Message message = Message.builder()
                .type(U_TEXT)
                .message(userMessage)
                .chat(chat)
                .build();
        message = messageRepository.save(message);


        // 현재 course를 jsonString 타입으로 파일에 저장
        List<Course> currentCourses = currentCourseList(chat);
        List<courseDTO> courseList = new ArrayList<>();
        for (Course course : currentCourses) {
            List<Place> places = course.getPlaceList().stream()
                    .sorted(Comparator.comparing(Place::getCourseOrder))
                    .collect(Collectors.toList());
            ;
            List<placeDTO> placeList = new ArrayList<>();
            for (Place place : places) {
                placeDTO dto = placeDTO.builder()
                        .placeId(place.getId().intValue())
                        .type(place.getType())
                        .placeName(place.getPlacename())
                        .comment(place.getComment())
                        .address(place.getAddress())
                        .url(place.getUrl())
                        .ratings(place.getRatings())
                        .build();
                placeList.add(dto);
            }
            courseDTO dto = courseDTO.builder()
                    .courseId(course.getId().intValue())
                    .day(course.getDay())
                    .places(placeList)
                    .build();
            courseList.add(dto);
        }
        TravelCourseResponse course = TravelCourseResponse.builder()
                .SIDO(chat.getSido())
                .SI(chat.getSi())
                .travelTitle(chat.getChatname())
                .days(currentCourses.size())
                .courses(courseList)
                .build();

        String arg_path = base_path+String.format("result/course_args_%d.txt", chatId);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            currentCourse = objectMapper.writeValueAsString(course);

            // 파일의 상위 디렉토리 생성
            File file = new File(arg_path);
            file.getParentFile().mkdirs();  // 상위 디렉토리 생성

            FileWriter writer = new FileWriter(arg_path);
            //System.out.println("currentCourse: "+ currentCourse);
            writer.write(currentCourse);
            writer.close();
            log.info("arg_path: " + arg_path);

        } catch (Exception e){
            e.printStackTrace();
        }

        // LLM 응답 생성 - chat_api.py 실행
        String scriptPath = base_path+"openai/chat_api.py";
        List<String> scriptArgs = Arrays.asList(userMessage, chatId.toString());

        pythonScriptRunner.runScript(scriptPath, scriptArgs);

        ObjectMapper objectMapper = new ObjectMapper();

        String chat_result_file = base_path+String.format("result/chat_api_result_%d.json", chatId);
        JsonNode chatApiJson = null;

        File f = new File(chat_result_file);
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            chatApiJson = objectMapper.readTree(reader);
        } catch (Exception e) {
            log.error("LLM 응답메세지 생성 실패 - chat_api.py 응답 없음");
            e.printStackTrace();  // 파일이 없거나 JSON 형식이 잘못되었을 경우 예외 처리
        } finally {
            // 파일 삭제 시도
            if (f.exists()) {
                boolean isDeleted = f.delete();
                if (isDeleted) {
                    log.info("파일이 성공적으로 삭제되었습니다: " + f.getPath());
                } else {
                    log.warn("파일 삭제에 실패했습니다: " + f.getPath());
                }
            }
        }

        // arg 파일 삭제
        File f2 = new File(arg_path);
        if (f2.exists()) {
            boolean isDeleted = f2.delete();
            if (isDeleted) {
                log.info("파일이 성공적으로 삭제되었습니다: " + f2.getPath());
            } else {
                log.warn("파일 삭제에 실패했습니다: " + f2.getPath());
            }
        }

        try{
            log.info("챗봇 응답 생성 완료");
            log.info(chatApiJson.toPrettyString());

            String function = chatApiJson.get("function").asText();
            List<Message> responseMessageList = new ArrayList<>();

            log.info("function: "+function);
            if (function.equals("1") || function.equals("2") || function.equals("3")){
                // 코스 변경 있음 -> 메시지 두 개 생성
                String text = "원하는 대로 코스를 다시 만들어 봤어!";
                Message message1 = Message.builder()
                        .type(C_TEXT)
                        .message(text)
                        .chat(chat)
                        .build();
                message1 = messageRepository.save(message1);

                Message message2 = Message.builder()
                        .type(C_COURSE)
                        .chat(chat)
                        .build();

                List<Course> mappingCourse = mappingCourse(chatApiJson.get("response"), message2);
                log.info(mappingCourse.toString());
                message2.setCourseList(mappingCourse);
                message2 = messageRepository.save(message2);

                responseMessageList.add(message1);
                responseMessageList.add(message2);

            } else if (function.equals("4") || function.equals("5") || function.equals("6")) {
                // 코스 변경 없음 - 메세지만
                String chatResponse = chatApiJson.get("response").asText();
                message = Message.builder()
                        .type(C_TEXT)
                        .message(chatResponse)
                        .chat(chat)
                        .build();
                message = messageRepository.save(message);
                responseMessageList.add(message);
            }

            SendMessageResponse response = chatConverter.toSendMessageResponse(chat, responseMessageList);
            return response;

        } catch (Exception e){
            e.printStackTrace();
            // 대화 응답 생성 실패 오류 문구 반환
            String text = "내가 잘 이해하지 못한 것 같아. 다시 설명해줄래?\n 일정을 조정 하거나, 여행지 정보를 알려주는 도움을 줄 수 있어!";
            List<Message> responseMessageList = new ArrayList<>();
            Message errorMessage = Message.builder()
                    .type(C_TEXT)
                    .message(text)
                    .chat(chat)
                    .build();
            errorMessage = messageRepository.save(errorMessage);
            responseMessageList.add(errorMessage);
            SendMessageResponse response = chatConverter.toSendMessageResponse(chat, responseMessageList);
            return response;
        }

    }

    public SaveTravelResponse saveTravel(Integer chatId, SaveTravelRequest request) {
        Optional<Chat> chatOptional = chatRepository.findById(Long.valueOf(chatId));
        if (chatOptional.isEmpty()){
            //예외처리
        }
        Chat chat = chatOptional.get();
        // 채팅
        if (chat.getStatus() == COMPLETE){
            log.error("이미 종료된 채팅입니다.");
        }
        chat.setStatus(COMPLETE);
        chatRepository.save(chat);

        Optional<Message> messageOptional = messageRepository.findById(Long.valueOf(request.getMessageId()));
        if (messageOptional.isEmpty()){
            //예외처리
        }
        Message message = messageOptional.get();
        List<Course> finalCourse = message.getCourseList().stream()
                .sorted(Comparator.comparing(Course::getDay))
                .collect(Collectors.toList());

        // Travel 생성
        // User - 로그인 기능 추가시 개발
        //User user = ..

        Travel travel = Travel.builder()
                .username(chat.getUsername())
                .travelTitle(chat.getChatname())
                .region(chat.getRegion())
                .sido(chat.getSido())
                .si(chat.getSi())
                .days(finalCourse.size())
                .image("")
                .courseList(finalCourse)
                .build();
        travel = travelRepository.save(travel);

        // travelId 저장
        for (Course course: finalCourse){
            course.setTravel(travel);
            courseRepository.save(course);
        }

        SaveTravelResponse response = chatConverter.toSaveTravelResponse(travel);
        return response;
    }

    // 추가 내부 메서드
    // 코스 리스트 생성
    private List<Course> createCourseList(int days,JsonNode courseResponse, Message message){
        List<Course> courseList = new ArrayList<>();
        for(int i=0; i<days; i++){
            CourseType coursetype = null;
            if (days == 1){
                coursetype = ONEDAY;
            } else if (i==0){
                coursetype = FIRST;
            } else if (i==days-1) {
                coursetype = LAST;
            } else{
                coursetype = MIDDLE;
            }

            Course course = Course.builder()
                    .day(i+1)
                    .courseType(coursetype)
                    .message(message)
                    .build();

            //place 매핑
            JsonNode places = courseResponse.get(String.format("day%d",i+1));
            List<Place> placeList = new ArrayList<>();
            int index = 1;
            for(JsonNode placeNode : places){
                if (index > 7){
                    break;
                }
                String type = "";
                // 장소 타입 - 여행지 / 숙소 / 식당 / 카페
                if (index == 2 || index == 5){
                    type = "식당";
                } else if (index == 3 || index == 6) {
                    type = "카페";
                } else if (index == 1 || index == 4 || index == 7){
                    type = "여행지";
                }

                if (coursetype == FIRST || coursetype == LAST){
                    if(index == 1){
                        type = "숙소";
                    }
                }

                Place place = Place.builder()
                        .course(course)
                        .placename(placeNode.get("place").asText())
                        .type(type)
                        .comment(placeNode.get("reason").asText())
                        .address(placeNode.get("address").asText())
                        .url(placeNode.get("place_url").asText())
                        .ratings(placeNode.get("ratings").asText())
                        .courseOrder(index)
                        .build();
                placeList.add(place);
                index++;
            }
            course.setPlaceList(placeList);
            courseList.add(course);
        }
        return courseList;
    }

    // 현재 채팅의 가장 최신 코스 리스트 반환
    private List<Course> currentCourseList(Chat chat){
        List<Message> messages = chat.getMessageList();
        Optional<Message> messageOptional = messages.stream()
                .filter(m -> m.getType().equals(C_COURSE)) // 특정 type 필터링
                .max(Comparator.comparing(Message::getCreatedAt)); // ID 값이 가장 큰 메시지 찾기

        if (messageOptional.isEmpty()){
            //예외처리
        }
        Message message = messageOptional.get();

        return message.getCourseList().stream()
                .sorted(Comparator.comparing(Course::getDay))
                .collect(Collectors.toList());

    }

    // chat-api 응답 매핑
    private List<Course> mappingCourse(JsonNode courseJson, Message message){
        Integer days = courseJson.get("days").asInt();
        JsonNode courses = courseJson.path("courses"); // "courses" 리스트 가져오기
        log.info(courses.toPrettyString());
        List<Course> courseList = new ArrayList<>();
        for (JsonNode courseNode : courses) {
            Integer day = courseNode.get("day").asInt();

            CourseType coursetype = null;
            if (days == 1) {
                coursetype = ONEDAY;
            } else if (day == 0) {
                coursetype = FIRST;
            } else if (day == days - 1) {
                coursetype = LAST;
            } else {
                coursetype = MIDDLE;
            }

            Course course = Course.builder()
                    .courseType(coursetype)
                    .message(message)
                    .day(day)
                    .build();

            JsonNode places = courseNode.path("places");
            List<Place> placeList = new ArrayList<>();
            int index = 1;
            for (JsonNode placeNode : places) {
                Place place = Place.builder()
                        .course(course)
                        .placename(placeNode.get("placeName").asText(""))
                        .type(placeNode.get("type").asText(""))
                        .comment(placeNode.get("comment").asText(""))
                        .address(placeNode.get("address").asText(""))
                        .url(placeNode.get("url").asText(""))
                        .ratings(placeNode.get("ratings").asText(""))
                        .courseOrder(index)
                        .build();
                placeList.add(place);
                index++;
            }
            course.setPlaceList(placeList);
            courseList.add(course);
        }
        return courseList;
    }
}
