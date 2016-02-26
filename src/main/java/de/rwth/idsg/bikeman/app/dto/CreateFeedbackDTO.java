package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
public class CreateFeedbackDTO {
    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
