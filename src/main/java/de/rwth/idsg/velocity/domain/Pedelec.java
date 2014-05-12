package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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

    @Column(name = "state")
    private Boolean state;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pedelec")
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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
