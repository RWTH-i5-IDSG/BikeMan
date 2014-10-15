package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.Data;

import java.util.List;

/**
 * Created by max on 01/10/14.
 */
@Data
public class BookingTargetsInfoResponseDTO {
    private long timestamp;
    private List<PedelecDTO> pedelecs;
    private List<StationDTO> stations;
}