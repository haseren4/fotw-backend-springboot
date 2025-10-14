package wis.fotabackend.dto;

import lombok.Getter;
import lombok.Setter;

public class CreateActivationDTO {
    @Getter @Setter
    private int userId;

    @Getter @Setter
    private int siteId; // corresponds to site_code

    @Getter @Setter
    private String startTime;

    @Getter @Setter
    private String endTime;

    @Getter @Setter
    private String logStatus;

    @Getter @Setter
    private String submittedAt;
}
