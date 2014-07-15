package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by sgokay on 20.05.14.
 */
@Getter
@ToString(includeFieldNames = true)
public class ViewPedelecDTO {

    private Long pedelecId;
    private String manufacturerId;
    private Float stateOfCharge;
    private OperationState state;
    private Boolean inTransaction;
    private ViewStationDTO station;
    private ViewTransactionDTO transaction;

    // Basic
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;
    }

    // Constructor for stationary pedelecs
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction,
                          Long stationId, String stationManufacturerId, Integer stationSlotPosition) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;

        this.station = new ViewStationDTO(stationId, stationManufacturerId, stationSlotPosition);
    }

    // Constructor for pedelecs in transaction
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction,
                          String customerId, String customerFirstname, String customerLastname,
                          Long stationId, Integer stationSlotPosition, LocalDateTime startDateTime) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;

        this.transaction = new ViewTransactionDTO(customerId,
                customerFirstname, customerLastname,
                stationId, stationSlotPosition, startDateTime);
    }

    @Data
    private class ViewStationDTO {
        private final Long id;
        private final String stationManufacturerId;
        private final Integer stationSlotPosition;
    }

    @Data
    private class ViewTransactionDTO {
        private final String customerId;
        private final String firstname;
        private final String lastname;
        private final Long lastStationId;
        private final Integer lastStationSlotPosition;

        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        private final LocalDateTime startDateTime;
    }
}
