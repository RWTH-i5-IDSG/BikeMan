package de.rwth.idsg.bikeman.domain;

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
@EqualsAndHashCode(callSuper = false, of = {"pedelecId", "manufacturerId"})
@ToString(includeFieldNames = true, exclude = {"transactions", "stationSlot"})
@Getter
@Setter
public class Pedelec extends AbstractTimestampClass implements Serializable {
    private static final long serialVersionUID = 7187208000731589081L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "pedelec_gen")
    @Column(name = "pedelec_id")
    private long pedelecId;

    @Column(name = "manufacturer_id")
    private String manufacturerId;

    @Column(name = "in_transaction")
    private Boolean inTransaction;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OperationState state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedelec")
    private Set<Transaction> transactions;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "pedelec", cascade = CascadeType.ALL)
    private PedelecChargingStatus chargingStatus;

    @OneToOne(mappedBy = "pedelec")
    private StationSlot stationSlot;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_info")
    private String errorInfo;

    @PrePersist
    public void prePersist() {
        super.prePersist();

        if (inTransaction == null) {
            inTransaction = false;
        }
    }
}
