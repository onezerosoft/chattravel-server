package onezerosoft.chattravel.converter;

import lombok.RequiredArgsConstructor;
import onezerosoft.chattravel.domain.Course;
import onezerosoft.chattravel.domain.Place;
import onezerosoft.chattravel.domain.Travel;
import onezerosoft.chattravel.web.dto.courseDTO;
import onezerosoft.chattravel.web.dto.placeDTO;
import onezerosoft.chattravel.web.dto.travel.TravelCourseResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelConverter {
    public static TravelCourseResponse toTravelCourseResponse(Travel travel){
        List<Course> courses = travel.getCourseList().stream()
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
                        .placeType(place.getType())
                        .placeName(place.getPlacename())
                        .comment(place.getComment())
                        .address(place.getAddress())
                        .url(place.getUrl())
                        .ratings(place.getRatings())
                        .SIDO(place.getSido())
                        .SI(place.getSi())
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

        return TravelCourseResponse.builder()
                .SIDO(travel.getSido())
                .SI(travel.getSi())
                .travelTitle(travel.getTravelTitle())
                .days(travel.getDays())
                .courses(courseList)
                .build();
    }
}
