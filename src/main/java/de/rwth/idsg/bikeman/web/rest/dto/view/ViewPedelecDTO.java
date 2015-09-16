package de.rwth.idsg.bikeman.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
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
    private Double stateOfCharge;
    private OperationState state;
    private Boolean inTransaction;
    private ViewStationDTO station;
    private ViewTransactionDTO transaction;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime lastChargingUpdate;


    // Basic
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Double stateOfCharge,
                          OperationState state, Boolean inTransaction) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;
    }

    // Constructor for stationary pedelecs
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Double stateOfCharge,
                          OperationState state, Boolean inTransaction,
                          Long stationId, String stationName,
                          Integer stationSlotPosition, LocalDateTime lastChargingUpdate) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;
        this.lastChargingUpdate = lastChargingUpdate;

        this.station = new ViewStationDTO(stationId, stationName, stationSlotPosition);
    }

    // Constructor for pedelecs in transaction with customer
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Double stateOfCharge,
                          OperationState state, Boolean inTransaction, String cardId,
                          String customerId, String customerFirstname, String customerLastname,
                          Long stationId, Integer stationSlotPosition, LocalDateTime startDateTime) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;

        this.transaction = new ViewTransactionDTO(cardId, null, customerId,
                customerFirstname, customerLastname,
                stationId, stationSlotPosition, startDateTime);
    }

    // Constructor for pedelecs in transaction with major customer
    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Double stateOfCharge,
                          OperationState state, Boolean inTransaction, String cardId,
                          String name,
                          Long stationId, Integer stationSlotPosition, LocalDateTime startDateTime) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;

        this.transaction = new ViewTransactionDTO(cardId, name, null,
                null, null,
                stationId, stationSlotPosition, startDateTime);
    }

    @Data
    private class ViewStationDTO {
        private final Long id;
        private final String stationName;
        private final Integer stationSlotPosition;
    }

    @Data
    private class ViewTransactionDTO {
        private final String cardId;
        private final String majorCustomerName;
        private final String customerId;
        private final String firstname;
        private final String lastname;
        private final Long lastStationId;
        private final Integer lastStationSlotPosition;

        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        private final LocalDateTime startDateTime;
    }
}
