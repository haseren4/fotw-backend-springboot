package wis.fotabackend.dto;

import lombok.Getter;
import lombok.Setter;

public class CreateActivationPostDTO {
    @Getter @Setter
    private String callsign;

    @Getter @Setter
    private int activationId;

    @Getter @Setter
    private String postTime;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String body;
}
