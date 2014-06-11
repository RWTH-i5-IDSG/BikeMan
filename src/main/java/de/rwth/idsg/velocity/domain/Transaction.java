package de.rwth.idsg.velocity.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "T_TRANSACTION",
        indexes = {
                @Index(columnList="user_id", unique = false),
                @Index(columnList="pedelec_id", unique = false),
                @Index(columnList="from_slot_id", unique = false),
                @Index(columnList="to_slot_id", unique = false) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="transaction_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"transactionId"})
@ToString(includeFieldNames = true)
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "transaction_gen")
    @Column(name = "transaction_id")
    @Getter @Setter
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter @Setter
    private Customer customer;

    @Column(name = "start_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @Getter @Setter
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @Getter @Setter
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "pedelec_id")
    @Getter @Setter
    private Pedelec pedelec;

    @ManyToOne
    @JoinColumn(name = "from_slot_id")
    @Getter @Setter
    private StationSlot fromSlot;

    @ManyToOne
    @JoinColumn(name = "to_slot_id")
    @Getter @Setter
    private StationSlot toSlot;

}
