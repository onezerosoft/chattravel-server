package onezerosoft.chattravel.web.dto;

import lombok.Builder;

@Builder
public class placeDTO {
    private Integer placeId;
    private String type;
    private String placeName;
    private String comment;
    private String address;
}
