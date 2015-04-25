package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

@Getter
@Builder
@ToString(includeFieldNames = true)
public class ViewBookingDTO {
    private Long stationId;
    private Integer stationSlotPosition;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime expiryDateTime;
}
