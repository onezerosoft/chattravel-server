package onezerosoft.chattravel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import onezerosoft.chattravel.domain.Chat;
import onezerosoft.chattravel.domain.Course;
import onezerosoft.chattravel.domain.Message;
import onezerosoft.chattravel.domain.Place;
import onezerosoft.chattravel.python.PythonScriptRunner;
import onezerosoft.chattravel.repository.ChatRepository;
import onezerosoft.chattravel.service.ChatService;
import onezerosoft.chattravel.web.dto.courseDTO;
import onezerosoft.chattravel.web.dto.placeDTO;
import onezerosoft.chattravel.web.dto.travel.TravelCourseResponse;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static onezerosoft.chattravel.domain.enums.SendType.C_COURSE;

@SpringBootTest
@Transactional
class ChattravelApplicationTests {
	@Autowired
	private ChatRepository chatRepository;

	@Test
	void contextLoads() {
	}


	// 파이썬 스크립트 호출 테스트
	@Test
	public void runScriptTest(){
		PythonScriptRunner runner = new PythonScriptRunner();

		// 테스트 할 파이썬 스크립트 경로와 인자
		// String scriptPath = "E:/onezerosoft/chattravel-server/chattravel-recommend/src/prediction.py";
		// List<String> scriptArgs = Arrays.asList("guest4", "수도권", "서울시", "3", "1,2,3,4");
		String newDirPath = "E:/onezerosoft/chattravel-server";
		System.setProperty("user.dir", newDirPath);

		String scriptPath = "E:/onezerosoft/chattravel-server/chattravel-recommend/src/openai/course_api.py";
		List<String> scriptArgs = Arrays.asList("2", "서울역사박물관, 오브젝트 성수점, 한전아트센터, 창덕궁", "콘래드", "서울시");


		// 스크립트 실행 테스트
		String result = runner.runScript(scriptPath, scriptArgs);
	}


	// json 로드 테스트
	@Test
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

	// chat_api.py 호출 테스트
	@Test
	public void chatApiTest(){
		PythonScriptRunner runner = new PythonScriptRunner();

		String userMessage = "서울 역사 박물관 운영시간 알려줘";
		String currentCourse = "";

		Integer chatId = 8;
		Optional<Chat> chatOptional = chatRepository.findById(Long.valueOf(chatId));
		if (chatOptional.isEmpty()){
			//예외처리
		}
		Chat chat = chatOptional.get();

		// 현재 course를 jsonString 타입으로 전달
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
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			currentCourse = objectMapper.writeValueAsString(course);

		} catch (Exception e){
			e.printStackTrace();
		}

		// 호출
		String newDirPath = "E:/onezerosoft/chattravel-server";
		System.setProperty("user.dir", newDirPath);

		String scriptPath = "E:/onezerosoft/chattravel-server/chattravel-recommend/src/openai/chat_api.py";
		List<String> scriptArgs = Arrays.asList(userMessage, currentCourse);

		runner.runScript(scriptPath, scriptArgs);

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


}
