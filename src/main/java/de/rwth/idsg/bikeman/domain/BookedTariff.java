package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Wolfgang Kluth on 16/01/15.
 */

@Entity
@Table(name = "T_BOOKED_TARIFF")
@TableGenerator(name = "booked_tariff_gen", initialValue = 0, allocationSize = 1)
@EqualsAndHashCode(of = {"bookedTariffId"})
@ToString(includeFieldNames = true)
@Getter
@Setter
public class BookedTariff extends AbstractTimestampClass {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "booked_tariff_gen")
    @Column(name = "booked_tariff_id")
    private Long bookedTariffId;

    @Column(name = "booked_from")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime bookedFrom;

    @Column(name = "booked_until")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime bookedUntil;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @OneToOne
    @JoinColumn(name = "used_card_account_id")
    private CardAccount usedCardAccount;

    @ManyToOne
    @JoinColumn(name = "card_account_id")
    private CardAccount cardAccount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookedTariff")
    private Set<Transaction> transactions;

    @Transient
    public TariffType getName() {
        return getTariff().getName();
    }

    @Transient
    public Boolean isActive() {
        return getTariff().getActive();
    }

    @Transient
    public Float getMounthlyRate() {
        return getTariff().getMounthlyRate();
    }
}
