package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateTimeDeserializer;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Transaction.
 */
@Entity
@Table(name = "T_TRANSACTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Integer id;

    @ManyToOne
    @JsonBackReference("customer_transactions")
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name="start_datetime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startDateTime;

    @Column(name="end_datetime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endDateTime;

    @OneToOne
    @JsonBackReference("pedelec_current_transaction")
    @JoinColumn(name="pedelec_id")
    private Pedelec pedelec;

    @ManyToOne
    @JsonBackReference("transaction_fromslot")
    @JoinColumn(name="from_slot_id")
    private StationSlot fromSlot;

    @ManyToOne
    @JsonBackReference("transaction_toslot")
    @JoinColumn(name="to_slot_id")
    private StationSlot toSlot;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction transaction = (Transaction) o;

        if (id != transaction.id) {
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
//        return "Transaction{" +
//                "id=" + id +
//                ", sampleTextAttribute='" + sampleTextAttribute + '\'' +
//                ", sampleDateAttribute=" + sampleDateAttribute +
//                '}';
//    }
}
