package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Builder
@ToString(includeFieldNames = true)
public class ViewStationSlotsDTO {

    private List<StationSlotsDTO> stationSlots;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long recommendedSlot;

}
