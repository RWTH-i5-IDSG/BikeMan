package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A Station.
 */
@Entity
@Table(name = "T_STATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Station implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "station", orphanRemoval = true)
    @JsonManagedReference("station_station_slots")
    private Set<StationSlot> stationSlots;

    @Column(name = "location_latitude", scale = 18, precision = 24)
    private BigDecimal locationLatitude;

    @Column(name = "location_longitude", scale = 18, precision = 24)
    private BigDecimal locationLongitude;

    @Lob
    @Column(name = "note")
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<StationSlot> getStationSlots() {
        return stationSlots;
    }

    public void setStationSlots(Set<StationSlot> stationSlots) {
        this.stationSlots = stationSlots;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Station station = (Station) o;

        if (id != station.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

//    @Override
//    public String toString() {
//        return "Station{" +
//                "id=" + id +
//                ", sampleTextAttribute='" + sampleTextAttribute + '\'' +
//                ", sampleDateAttribute=" + sampleDateAttribute +
//                '}';
//    }
}
