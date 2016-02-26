package de.rwth.idsg.bikeman.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;

@Getter
@ToString(includeFieldNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewTransactionDTO {

    private Long transactionId;
    private TransactionStationDTO fromStation;
    private TransactionStationDTO toStation;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    private Long distance;
    private BigDecimal fees;

    @Data
    class TransactionStationDTO {
        private final Long stationId;
        private final String name;
    }

    public ViewTransactionDTO (Long transactionId,
                               LocalDateTime startDateTime, LocalDateTime endDateTime,
                               BigDecimal fees,
                               Long fromStationId, String fromStationName,
                               Long toStationId, String toStationName) {

        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.fees = fees;

        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName);
        this.toStation = new TransactionStationDTO(toStationId, toStationName);

        this.distance = 1300l;
    }

    public ViewTransactionDTO (Long transactionId,
                               LocalDateTime startDateTime,
                               Long fromStationId, String fromStationName) {

        this.transactionId = transactionId;
        this.startDateTime = startDateTime;
        this.fromStation = new TransactionStationDTO(fromStationId, fromStationName);
        this.distance = null;
    }
}
