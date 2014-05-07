package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.Set;

/**
 * A Pedelec.
 */
@Entity
@Table(name = "T_PEDELEC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pedelec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Integer id;

    @Column(name = "state_of_charge")
    private Float stateOfCharge;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @OneToMany(mappedBy = "pedelec")
    @JsonManagedReference("pedelec_transactions")
    private Set<Transaction> transactions;

    @OneToOne(mappedBy = "pedelec")
    @JsonManagedReference("pedelec_station_slot")
    private StationSlot stationSlot;

    @OneToOne(mappedBy = "pedelec")
    @JsonManagedReference("pedelec_current_transaction")
    private Transaction currentTransaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pedelec pedelec = (Pedelec) o;

        if (id != pedelec.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Pedelec{" +
                "id=" + id +
//                ", sampleTextAttribute='" + sampleTextAttribute + '\'' +
//                ", sampleDateAttribute=" + sampleDateAttribute +
                '}';
    }
}
