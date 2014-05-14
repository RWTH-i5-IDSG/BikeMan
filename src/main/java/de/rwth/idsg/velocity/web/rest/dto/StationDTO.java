package de.rwth.idsg.velocity.web.rest.dto;

import de.rwth.idsg.velocity.domain.Address;

import java.math.BigDecimal;

/**
 * Created by max on 12/05/14.
 */
public class StationDTO {

    public StationDTO () {
    }

    public StationDTO(String name, BigDecimal locationLatitude, BigDecimal locationLongitude, Address address, int numberOfSlots) {
        this.name = name;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.address = address;
        this.numberOfSlots = numberOfSlots;
    }

    private String name;

    private BigDecimal locationLatitude;

    private BigDecimal locationLongitude;

    private Address address;

    private int numberOfSlots;

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
        return "StationDTO{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", locationLatitude=" + locationLatitude +
                ", locationLongitude=" + locationLongitude +
                ", numberOfSlots=" + numberOfSlots +
                '}';
    }
}
