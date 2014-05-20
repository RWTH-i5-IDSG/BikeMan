package de.rwth.idsg.velocity.web.rest.dto;

import de.rwth.idsg.velocity.domain.Address;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * Created by max on 12/05/14.
 */
public class StationDTO {

    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal locationLatitude;

    @NotBlank
    private BigDecimal locationLongitude;

    @NotBlank
    private Address address;

    @NotBlank
    private int numberOfSlots;

    public StationDTO () {
    }

    public StationDTO(String name, BigDecimal locationLatitude, BigDecimal locationLongitude, Address address, int numberOfSlots) {
        this.name = name;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.address = address;
        this.numberOfSlots = numberOfSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(BigDecimal locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public BigDecimal getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(BigDecimal locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("address", address)
                .append("locationLatitude", locationLatitude)
                .append("locationLongitude", locationLongitude)
                .append("numberOfSlots", numberOfSlots)
                .toString();
    }
}
