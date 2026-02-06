package wis.fotabackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIssueDTO {
    private String email;
    private String title;
    private String body;
}
