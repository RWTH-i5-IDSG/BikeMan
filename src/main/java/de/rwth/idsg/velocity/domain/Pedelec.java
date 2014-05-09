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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "pedelec_id")
    private String pedelecId;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPedelecId() {
        return pedelecId;
    }

    public void setPedelecId(String pedelecId) {
        this.pedelecId = pedelecId;
    }

    public Float getStateOfCharge() {
        return stateOfCharge;
    }

    public void setStateOfCharge(Float stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public StationSlot getStationSlot() {
        return stationSlot;
    }

    public void setStationSlot(StationSlot stationSlot) {
        this.stationSlot = stationSlot;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

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
