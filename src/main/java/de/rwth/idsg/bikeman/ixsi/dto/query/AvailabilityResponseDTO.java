package de.rwth.idsg.bikeman.ixsi.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Created by max on 06/10/14.
 */
@Data
//@SqlResultSetMapping(name = "availabilityResult", classes = {
//        @ConstructorResult(targetClass = AvailabilityResponseDTO.class,
//                columns = {
//                        @ColumnResult(name = "pedelecId"),
//                        @ColumnResult(name = "stationId"),
//                        @ColumnResult(name = "locationLatitude"),
//                        @ColumnResult(name = "locationLongitude"),
//                        @ColumnResult(name = "stateOfCharge")
//                })
//})
public class AvailabilityResponseDTO {

    private final BigInteger pedelecId;
    private final BigInteger stationId;
    private final BigDecimal locationLatitude;
    private final BigDecimal locationLongitude;
    private final Float stateOfCharge;
//    private Float drivingRange;


}
