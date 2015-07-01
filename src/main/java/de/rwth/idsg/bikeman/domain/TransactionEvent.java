package de.rwth.idsg.bikeman.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;

/**
 * Created by max on 26/06/15.
 */
@Entity
@Table(name = "T_TRANSACTION_EVENT")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="transaction_event_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode
@Getter
@Setter
public class TransactionEvent implements Serializable {
    private static final long serialVersionUID = 855587893926001422L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "transaction_event_gen")
    @Column(name = "transaction_event_id")
    private Long transactionEventId;

    @Column(name = "arrived_timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime arrivedTimestamp;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "timestamp")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "card_account_id")
    private CardAccount cardAccount;

    @ManyToOne
    @JoinColumn(name = "station_slot_id")
    private StationSlot stationSlot;

    @ManyToOne
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;

}
