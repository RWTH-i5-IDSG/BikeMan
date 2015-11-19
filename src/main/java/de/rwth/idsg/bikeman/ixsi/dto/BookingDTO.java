package de.rwth.idsg.bikeman.ixsi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 19.11.2015
 */
@ToString
@EqualsAndHashCode
@Getter
public class BookingDTO {
    private String ixsiBookingId;
    private DateTime reservationStart;

    public BookingDTO(String ixsiBookingId, LocalDateTime reservationStart) {
        this.ixsiBookingId = ixsiBookingId;
        this.reservationStart = reservationStart.toDateTime();
    }

}
