package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by max on 01/10/14.
 */
@Getter
@Setter
@ToString
public class BookingTargetsInfoResponseDTO {
    private long timestamp;
    private List<PedelecDTO> pedelecs;
    private List<StationDTO> stations;
}