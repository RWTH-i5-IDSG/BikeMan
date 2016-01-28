package de.rwth.idsg.bikeman.app.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Setter
@Getter
public class CreateStationSupportDTO {
    public enum StationErrorCode {
        TERMINAL,
        SCREEN,
        SOILED,
        SLOT,
        MISC;
    }

    private Long stationId;

    @Valid
    private StationErrorCode error;

    private String content;
}
