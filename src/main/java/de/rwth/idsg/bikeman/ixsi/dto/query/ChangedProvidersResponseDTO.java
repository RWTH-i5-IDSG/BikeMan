package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

/**
 * Created by max on 06/10/14.
 */
@AllArgsConstructor
@Getter
public class ChangedProvidersResponseDTO {
    private XMLGregorianCalendar timestamp;
    private List<Object> providers;
}
