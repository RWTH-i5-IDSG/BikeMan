package de.rwth.idsg.bikeman.web.rest.dto.view;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.bikeman.domain.ErrorType;
import de.rwth.idsg.bikeman.domain.util.CustomLocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * Created by Wolfgang Kluth on 19/02/16.
 */

@Getter
@ToString(includeFieldNames = true)
@Builder
public class ErrorHistoryEntryDTO {
    private ErrorType errorType;
    private String errorCode;
    private String errorInfo;
    private String manufacturerId;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
}
