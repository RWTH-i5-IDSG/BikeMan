package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by sgokay on 20.05.14.
 */
@ToString(includeFieldNames = true)
public class ViewPedelecDTO {

    @Getter private Long pedelecId;
    @Getter private String manufacturerId;
    @Getter private Float stateOfCharge;
    @Getter private OperationState state;
    @Getter private Boolean inTransaction;
    @Getter private ViewStationDTO station;
    @Getter private ViewTransactionDTO transaction;

    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction,
                          Long stationaryStationId, String stationManufacturerId, Integer stationarySlotPosition,
                          String customerId, String customerFirstname, String customerLastname, Long lastStationId,
                          Integer lastSlotPosition, LocalDateTime startDateTime) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;

        //// For the following: Reverse if clause checks to prevent unnecessary negation otherwise ////

        if (customerId == null) {
            this.station = new ViewStationDTO(stationaryStationId, stationManufacturerId, stationarySlotPosition);
        }

        if (stationaryStationId == null) {
            this.transaction = new ViewTransactionDTO(customerId,
                    customerFirstname, customerLastname,
                    lastStationId, lastSlotPosition, startDateTime);
        }
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    private class ViewStationDTO {

        @Getter private Long id;
        @Getter private String stationManufacturerId;
        @Getter private Integer stationSlotPosition;
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    private class ViewTransactionDTO {

        @Getter private String customerId;
        @Getter private String firstname;
        @Getter private String lastname;
        @Getter private Long lastStationId;
        @Getter private Integer lastStationSlotPosition;

        @Getter
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        private LocalDateTime startDateTime;
    }
}
