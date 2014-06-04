package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 23/05/14.
 */

@ToString(includeFieldNames = true)
public class ViewTransactionDTO {

    @Getter private Long transactionId;
    @Getter private TransactionStationDTO fromStation;
    @Getter private TransactionStationDTO toStation;
    @Getter private CustomerDTO customer;
    @Getter private TransactionPedelecDTO pedelec;

    @Getter
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @Getter
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    // All and closed transactions
    public ViewTransactionDTO(Long transactionId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              Long toStationId, String toStationName, Integer toStationSlotPosition,
                              String customerId, String customerFirstname, String customerLastname,
                              Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customer = new CustomerDTO(customerId, customerFirstname + " " + customerLastname);

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);
        this.toStation = new TransactionStationDTO(toStationId, toStationName, toStationSlotPosition);

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    // Open transactions
    public ViewTransactionDTO(Long transactionId, LocalDateTime startDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              String customerId, String customerFirstname, String customerLastname,
                              Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.customer = new CustomerDTO(customerId, customerFirstname + " " + customerLastname);

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    class TransactionStationDTO {

        @Getter private Long stationId;
        @Getter private String name;
        @Getter private Integer slotPosition;
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    class TransactionPedelecDTO {

        @Getter private Long pedelecId;
        @Getter private String manufacturerId;
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = true)
    class CustomerDTO {

        @Getter private String customerId;
        @Getter private String customerName;
    }

}