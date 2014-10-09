package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

/**
 * Created by max on 01/10/14.
 */
@AllArgsConstructor
@Getter
public class BookingTargetsInfoResponseDTO {
    private long timestamp;
    private List<PedelecDTO> pedelecs;
    private List<StationDTO> stations;
    // TODO implement PlaceGroup equivalent ("Großkunden")
    private List<Object> placeGroups;
    // TODO implement Provider equivalent
    private List<Object> providers;
    // TODO implement Attribute equivalent
    private List<Object> attributes;
}
