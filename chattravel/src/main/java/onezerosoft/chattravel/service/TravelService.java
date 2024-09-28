package onezerosoft.chattravel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onezerosoft.chattravel.converter.TravelConverter;
import onezerosoft.chattravel.domain.Chat;
import onezerosoft.chattravel.domain.Travel;
import onezerosoft.chattravel.repository.TravelRepository;
import onezerosoft.chattravel.web.dto.travel.TravelCourseResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TravelService {
    private final TravelConverter travelConverter;
    private final TravelRepository travelRepository;

    public TravelCourseResponse getTravelCourse(String travelId){
        Optional<Travel> travelOptional = travelRepository.findById(Long.valueOf(travelId));
        if (travelOptional.isEmpty()){
            //예외처리
        }
        Travel travel = travelOptional.get();

        TravelCourseResponse response = travelConverter.toTravelCourseResponse(travel);
        return response;
    }
}
