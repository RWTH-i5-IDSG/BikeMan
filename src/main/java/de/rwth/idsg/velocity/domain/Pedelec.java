package de.rwth.idsg.velocity.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "T_PEDELEC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="pedelec_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"pedelecId", "manufacturerId"})
@ToString(includeFieldNames = true)
public class Pedelec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pedelec_gen")
    @Column(name = "pedelec_id")
    @Getter @Setter
    private long pedelecId;

    @Column(name = "manufacturer_id")
    @Getter @Setter
    private String manufacturerId;

    @Column(name = "state_of_charge")
    @Getter @Setter
    private Float stateOfCharge;

    @Column(name = "in_transaction")
    @Getter @Setter
    private Boolean inTransaction;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private OperationState state;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pedelec")
    @Getter @Setter
    private Set<Transaction> transactions;

    @OneToOne(mappedBy = "pedelec")
    @Getter @Setter
    private StationSlot stationSlot;

    @PrePersist
    public void prePersist() {
        if (stateOfCharge == null) {
            stateOfCharge = 0.0f;
        }
    }

}
