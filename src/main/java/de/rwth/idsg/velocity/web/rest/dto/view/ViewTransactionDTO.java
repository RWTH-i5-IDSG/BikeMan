package de.rwth.idsg.velocity.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.Customer;
import de.rwth.idsg.velocity.web.rest.dto.util.CustomLocalDateTimeSerializer;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by swam on 23/05/14.
 */

@ToString(includeFieldNames = true)
public class ViewTransactionDTO {

    @Getter
    private Long transactionId;

    @Getter
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime fromDateTime;

    @Getter
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime toDateTime;

    @Getter
    private TransactionStationDTO fromStation;

    @Getter
    private TransactionStationDTO toStation;

    @Getter
    private Customer customer;

    @Getter
    private TransactionPedelecDTO pedelec;

    public ViewTransactionDTO(Long transactionId, LocalDateTime fromDateTime, LocalDateTime toDateTime,
                              Long fromStationId, String fromStationName, Integer fromStationSlotPosition,
                              Long toStationId, String toStationName, Integer toStationSlotPosition,
                              Customer customer, Long pedelecId, String pedelecManufacturerId) {
        this.transactionId = transactionId;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.customer = customer;

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName, fromStationSlotPosition);
        this.toStation = new TransactionStationDTO(toStationId, toStationName, toStationSlotPosition);

        this.pedelec = new TransactionPedelecDTO(pedelecId, pedelecManufacturerId);
    }

    class TransactionStationDTO {
        @Getter
        private Long stationId;
        @Getter
        private String name;
        @Getter
        private Integer slotPosition;

        TransactionStationDTO(Long stationId, String name, Integer slotPosition) {
            this.stationId = stationId;
            this.name = name;
            this.slotPosition = slotPosition;
        }
    }

    class TransactionPedelecDTO {
        @Getter
        private Long pedelecId;
        @Getter
        private String manufacturerId;

        TransactionPedelecDTO(Long pedelecId, String manufacturerId) {
            this.pedelecId = pedelecId;
            this.manufacturerId = manufacturerId;
        }
    }

}
