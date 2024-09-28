package onezerosoft.chattravel.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class placeDTO {
    private Integer placeId;
    private String type;
    private String placeName;
    private String comment;
    private String address;
}
