package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Setter
@Getter
public class CreatePedelecSupportDTO {
    public enum PedelecErrorCode {
        SOILED,
        MISC;
    }

    private Long pedelecId;

    @Valid
    private PedelecErrorCode error;

    private String content;
}
