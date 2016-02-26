package de.rwth.idsg.bikeman.web.rest.dto.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.ErrorType;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.util.Date;

/**
 * Created by Wolfgang Kluth on 15/12/15.
 */
@Getter
@ToString(includeFieldNames = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewErrorDTO {

    private Long stationId;
    private Long slotId;
    private Long pedelecId;

    private String stationManufacturerId;
    private String slotManufacturerId;
    private String pedelecManufacturerId;

    private String stationName;
    private Integer slotPosition;

    private String errorCode;
    private String errorInfo;
    private ErrorType type;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;

    // Constructor for StationError
    public ViewErrorDTO(Long stationId, String stationManufacturerId, String stationName,
                        String errorCode, String errorInfo, Date lastUpdated) {
        this.stationId = stationId;
        this.stationManufacturerId = stationManufacturerId;
        this.stationName = stationName;
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
        this.lastUpdated = new LocalDateTime(lastUpdated);
        this.type = ErrorType.STATION_ERROR;
    }

    // Constructor for SlotError
    public ViewErrorDTO(Long stationId, Long slotId, String stationManufacturerId, String slotManufacturerId,
                        String stationName, int slotPosition, String errorCode, String errorInfo, Date lastUpdated) {
        this.stationId = stationId;
        this.slotId = slotId;
        this.stationManufacturerId = stationManufacturerId;
        this.slotManufacturerId = slotManufacturerId;
        this.stationName = stationName;
        this.slotPosition = slotPosition;
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
        this.lastUpdated = new LocalDateTime(lastUpdated);
        this.type = ErrorType.SLOT_ERROR;
    }

    // Constructor for PedelecError
    public ViewErrorDTO(Long pedelecId, String pedelecManufacturerId,
                        String errorCode, String errorInfo, Date lastUpdated) {
        this.pedelecId = pedelecId;
        this.pedelecManufacturerId = pedelecManufacturerId;
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
        this.lastUpdated = new LocalDateTime(lastUpdated);
        this.type = ErrorType.PEDELEC_ERROR;
    }
}
