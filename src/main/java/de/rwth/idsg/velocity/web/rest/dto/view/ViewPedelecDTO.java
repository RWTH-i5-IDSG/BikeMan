package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.OperationState;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateTimeSerializer;
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

    // Constructor for stationary pedelecs
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction,
                          Long stationId, String stationManufacturerId, Long stationSlotPosition) {
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
                          Long stationId, Long stationSlotPosition, LocalDateTime startDateTime) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;
        this.transaction = new ViewTransactionDTO(customerId, customerFirstname + customerLastname,
                stationId, stationSlotPosition, startDateTime);
    }

    @ToString(includeFieldNames = true)
    class ViewStationDTO {

        @Getter private Long id;
        @Getter private String stationManufacturerId;
        @Getter private Long stationSlotPosition;

        ViewStationDTO(Long id, String stationManufacturerId, Long stationSlotPosition) {
            this.id = id;
            this.stationManufacturerId = stationManufacturerId;
            this.stationSlotPosition = stationSlotPosition;
        }
    }

    @ToString(includeFieldNames = true)
    class ViewTransactionDTO {

        @Getter private String id;
        @Getter private String customerName;
        @Getter private Long lastStationId;
        @Getter private Long lastStationSlotPosition;

        @Getter
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        private LocalDateTime startDateTime;

        ViewTransactionDTO(String id, String customerName, Long lastStationId, Long lastStationSlotPosition, LocalDateTime startDateTime) {
            this.id = id;
            this.customerName = customerName;
            this.lastStationId = lastStationId;
            this.lastStationSlotPosition = lastStationSlotPosition;
            this.startDateTime = startDateTime;
        }
    }
}
