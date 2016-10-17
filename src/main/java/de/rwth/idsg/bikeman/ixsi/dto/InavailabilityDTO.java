package de.rwth.idsg.bikeman.ixsi.dto;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 30.07.2015
 */
@Setter
@Getter
public class InavailabilityDTO {

    private String pedelecManufacturerId;
    private DateTime begin;
    private DateTime end;

    public InavailabilityDTO(String pedelecManufacturerId, LocalDateTime localBegin, LocalDateTime localEnd) {
        this.pedelecManufacturerId = pedelecManufacturerId;
        this.begin = localBegin.toDateTime();

        if (localEnd == null) {
            end = IXSIConstants.constructReturnDateTime(localBegin.toDateTime());
        } else {
            end = localEnd.toDateTime();
        }
    }
}
