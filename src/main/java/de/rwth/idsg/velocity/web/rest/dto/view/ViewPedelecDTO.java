package de.rwth.idsg.velocity.web.rest.dto.view;

import de.rwth.idsg.velocity.domain.OperationState;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by sgokay on 20.05.14.
 */
@ToString(includeFieldNames = true)
public class ViewPedelecDTO {

    @Getter private Long pedelecId;
    @Getter private String manufacturerId;
    @Getter private Float stateOfCharge;
    @Getter private OperationState state;
    @Getter
    private Boolean inTransaction;
    @Getter
    private ViewStationDTO stationSlot;
    @Getter
    private ViewCustomerDTO customer;

    public ViewPedelecDTO(Long pedelecId, String manufacturerId, Float stateOfCharge,
                          OperationState state, Boolean inTransaction, Long stationId, String stationManufacturerId, Integer stationSlotPosition, Long customerId, String customerFullName) {
        this.pedelecId = pedelecId;
        this.manufacturerId = manufacturerId;
        this.stateOfCharge = stateOfCharge;
        this.state = state;
        this.inTransaction = inTransaction;
        this.stationSlot = new ViewStationDTO(stationId, stationManufacturerId, stationSlotPosition);
        this.customer = new ViewCustomerDTO(customerId, customerFullName);
    }

    @ToString(callSuper = true, includeFieldNames = true)
    class ViewStationDTO {

        @Getter private Long id;
        @Getter
        private String stationManufacturerId;
        @Getter
        private Integer stationSlotPosition;

        ViewStationDTO(Long id, String stationManufacturerId, Integer stationSlotPosition) {
            this.id = id;
            this.stationManufacturerId = stationManufacturerId;
            this.stationSlotPosition = stationSlotPosition;
        }
    }

    @ToString(callSuper = true, includeFieldNames = true)
    class ViewCustomerDTO {

        @Getter
        private Long id;
        @Getter
        private String fullname;

        ViewCustomerDTO(Long id, String fullname) {
            this.id = id;
            this.fullname = fullname;
        }
    }
}
