package onezerosoft.chattravel.converter;

import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.domain.*;
import onezerosoft.chattravel.domain.enums.SendType;
import onezerosoft.chattravel.web.dto.chat.ChatCreateResponse;
import onezerosoft.chattravel.web.dto.chat.SaveTravelResponse;
import onezerosoft.chattravel.web.dto.chat.SendMessageResponse;
import onezerosoft.chattravel.web.dto.chat.TotalMessageResponse;
import onezerosoft.chattravel.web.dto.contentDTO;
import onezerosoft.chattravel.web.dto.courseDTO;
import onezerosoft.chattravel.web.dto.messageDTO;
import onezerosoft.chattravel.web.dto.placeDTO;
import org.python.core.AstList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static onezerosoft.chattravel.domain.enums.SendType.*;

@Service
@RequiredArgsConstructor
public class ChatConverter {

    public static ChatCreateResponse toChatCreateResponse(Integer chatId){
        return ChatCreateResponse.builder()
                .chatId(chatId)
                .build();
    }

    public static TotalMessageResponse toTotalMessageResponse(Chat chat, List<Message> messages){
        List<messageDTO> messageList = new ArrayList<>();
        for (Message message : messages){
            // 메세지 타입별 콘텐츠 매핑
            SendType type = message.getType();
            contentDTO content = null;
            if (type == C_TEXT || type == U_TEXT) {
                 content = contentDTO.builder()
                        .message(message.getMessage())
                        .build();
            } else if (type == C_COURSE) {
                List<Course> courses = message.getCourseList().stream()
                        .sorted(Comparator.comparing(Course::getDay))
                        .collect(Collectors.toList());
                List<courseDTO> courseList = new ArrayList<>();
                for (Course course : courses) {
                    List<Place> places = course.getPlaceList().stream()
                            .sorted(Comparator.comparing(Place::getCourseOrder))
                            .collect(Collectors.toList());;
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
                content = contentDTO.builder()
                        .courses(courseList)
                        .build();
            }

            messageDTO dto = messageDTO.builder()
                    .messageId(message.getId().intValue())
                    .type(message.getType().toString())
                    .content(content)
                    .createdAt(message.getCreatedAt())
                    .build();
            messageList.add(dto);
        }
        return TotalMessageResponse.builder()
                .chatId(chat.getId().intValue())
                .chatname(chat.getChatname())
                .totalMessageCount(messageList.size())
                .messages(messageList)
                .createdAt(chat.getCreatedAt())
                .build();
    }

    public static SendMessageResponse toSendMessageResponse(Chat chat, List<Message> messages){

        List<messageDTO> messageList = new ArrayList<>();
        for (Message message : messages){
            // 메세지 타입별 콘텐츠 매핑
            SendType type = message.getType();
            contentDTO content = null;
            if (type == C_TEXT || type == U_TEXT) {
                content = contentDTO.builder()
                        .message(message.getMessage())
                        .build();
            } else if (type == C_COURSE) {
                List<Course> courses = message.getCourseList().stream()
                        .sorted(Comparator.comparing(Course::getDay))
                        .collect(Collectors.toList());
                List<courseDTO> courseList = new ArrayList<>();
                for (Course course : courses) {
                    List<Place> places = course.getPlaceList().stream()
                            .sorted(Comparator.comparing(Place::getCourseOrder))
                            .collect(Collectors.toList());;
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
                content = contentDTO.builder()
                        .courses(courseList)
                        .build();
            }

            messageDTO dto = messageDTO.builder()
                    .messageId(message.getId().intValue())
                    .type(message.getType().toString())
                    .content(content)
                    .createdAt(message.getCreatedAt())
                    .build();
            messageList.add(dto);
        }
        return SendMessageResponse.builder()
                .chatId(chat.getId().intValue())
                .totalMessageCount(messageList.size())
                .createdAt(chat.getCreatedAt())
                .messages(messageList)
                .build();
    }

    public static SaveTravelResponse toSaveTravelResponse(Travel travel){
        return SaveTravelResponse.builder()
                .travelId(travel.getId().intValue())
                .travelTitle(travel.getTravelTitle())
                .createdAt(travel.getCreatedAt())
                .build();
    }
}
