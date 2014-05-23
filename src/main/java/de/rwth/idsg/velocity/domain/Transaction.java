package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateTimeDeserializer;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateTimeSerializer;
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
@Table(name = "T_TRANSACTION")
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
    @JsonBackReference("customer_transactions")
    @JoinColumn(name = "customer_id")
    @Getter @Setter
    private Customer customer;

    @Column(name = "start_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Getter @Setter
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Getter @Setter
    private LocalDateTime endDateTime;

    @ManyToOne
    @JsonBackReference("pedelec_transactions")
    @JoinColumn(name = "pedelec_id")
    @Getter @Setter
    private Pedelec pedelec;

    @ManyToOne
    @JsonBackReference("transaction_fromslot")
    @JoinColumn(name = "from_slot_id")
    @Getter @Setter
    private StationSlot fromSlot;

    @ManyToOne
    @JsonBackReference("transaction_toslot")
    @JoinColumn(name = "to_slot_id")
    @Getter @Setter
    private StationSlot toSlot;

}
