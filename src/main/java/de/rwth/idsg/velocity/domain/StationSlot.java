package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A StationSlot.
 */
@Entity
@Table(name = "T_STATIONSLOT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StationSlot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @OneToOne
    @JsonBackReference("pedelec_station_slot")
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "station_station_slot")
    private Station station;

    @Column(name = "state")
    private Boolean state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pedelec getPedelec() {
        return pedelec;
    }

    public void setPedelec(Pedelec pedelec) {
        this.pedelec = pedelec;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StationSlot stationslot = (StationSlot) o;

        if (id != stationslot.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        // always hashed to the same value on creation! (id generationtype: table)
        return System.identityHashCode(this);
//        return (int) (id ^ (id >>> 32));
    }

//    @Override
//    public String toString() {
//        return "StationSlot{" +
//                "id=" + id +
//                ", sampleTextAttribute='" + sampleTextAttribute + '\'' +
//                ", sampleDateAttribute=" + sampleDateAttribute +
//                '}';
//    }
}
