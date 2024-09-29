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
	@Test
	void contextLoads() {
	}
}
