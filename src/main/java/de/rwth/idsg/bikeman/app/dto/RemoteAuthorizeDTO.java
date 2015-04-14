package de.rwth.idsg.bikeman.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString(includeFieldNames = true)
public class RemoteAuthorizeDTO {
    private Integer slotPosition;
    private String cardId;
}

