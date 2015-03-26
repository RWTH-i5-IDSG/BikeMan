package de.rwth.idsg.bikeman.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by swam on 06/10/14.
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_CARD_ACCOUNT",
        indexes = {@Index(columnList="card_id", unique = true)})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="card_account_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"cardId"}, callSuper = false)
@ToString(includeFieldNames = true, exclude = {})
@Getter
@Setter
public class CardAccount extends AbstractTimestampClass implements Serializable {
    private static final long serialVersionUID = -1059619797439373147L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "card_account_gen")
    @Column(name = "card_account_id")
    private Long cardAccountId;

    @Column(name = "card_id", unique = true)
    private String cardId;

    @Column(name = "card_pin")
    private String cardPin;

    @Column(name = "in_transaction")
    private Boolean inTransaction;

    @Column(name = "owner_type")
    @Enumerated(EnumType.STRING)
    private CustomerType ownerType;
    
    @Column(name = "activation_key")
    private String activationKey;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cardAccount", orphanRemoval = true)
    private Set<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "operation_state")
    @Enumerated(EnumType.STRING)
    private OperationState operationState;

    @Column(name = "authentication_trial_count")
    private Integer authenticationTrialCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cardAccount", orphanRemoval = true)
    private Set<BookedTariff> bookedTariffs;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usedCardAccount")
    private BookedTariff currentTariff;

    @PrePersist
    public void prePersist() {
        super.prePersist();

        if (inTransaction == null) {
            inTransaction = false;
        }
    }
    
    public void setCurrentTariff(BookedTariff bookedTariff) {
        if (this.currentTariff != null) {
            this.bookedTariffs.add(this.currentTariff);
            this.currentTariff.setCardAccount(this);
            this.currentTariff.setUsedCardAccount(null);
        }
        
        this.currentTariff = bookedTariff;
        bookedTariff.setUsedCardAccount(this);
    }
}
