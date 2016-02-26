package de.rwth.idsg.bikeman.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 23/05/14.
 */
@Getter
@ToString(includeFieldNames = true)
public class ViewTransactionDTO {

    private Long transactionId;
    private TransactionStationDTO fromStation;
    private TransactionStationDTO toStation;
    private CustomerDTO customer;
    private String majorCustomerName;
    private String cardId;
    private TransactionPedelecDTO pedelec;
    private String fleetManagerLogin;


    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    // Transactions related to a customer
    public ViewTransactionDTO(Long transactionId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              Long toStationId, String toStationName, Integer toStationSlotPosition,
                              String cardId, String customerId, String customerFirstname, String customerLastname,
                              Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.cardId = cardId;
        this.customer = new CustomerDTO(customerId, customerFirstname, customerLastname);

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);

        if (toStationId == null) {
            this.toStation = null;
        } else {
            this.toStation = new TransactionStationDTO(toStationId, toStationName, toStationSlotPosition);
        }

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    // Transactions related to a major customer
    public ViewTransactionDTO(Long transactionId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              Long toStationId, String toStationName, Integer toStationSlotPosition,
                              String cardId, String majorCustomerName,
                              Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.cardId = cardId;
        this.majorCustomerName = majorCustomerName;

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);

        if (toStationId == null) {
            this.toStation = null;
        } else {
            this.toStation = new TransactionStationDTO(toStationId, toStationName, toStationSlotPosition);
        }

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    // Transactions related to a fleet manager
    public ViewTransactionDTO(String fleetManagerLogin, Long transactionId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              Long toStationId, String toStationName, Integer toStationSlotPosition,
                              String cardId,
                              Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.cardId = cardId;
        this.fleetManagerLogin = fleetManagerLogin;

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);

        if (toStationId == null) {
            this.toStation = null;
        } else {
            this.toStation = new TransactionStationDTO(toStationId, toStationName, toStationSlotPosition);
        }

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    @Data
    class TransactionStationDTO {
        private final Long stationId;
        private final String name;
        private final Integer slotPosition;
    }

    @Data
    class TransactionPedelecDTO {
        private final Long pedelecId;
        private final String manufacturerId;
    }

    @Data
    class CustomerDTO {
        private final String customerId;
        private final String firstname;
        private final String lastname;
    }
}
