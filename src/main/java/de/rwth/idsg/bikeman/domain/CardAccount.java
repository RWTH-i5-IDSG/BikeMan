package de.rwth.idsg.bikeman.domain;

import de.rwth.idsg.bikeman.domain.login.User;
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
@Table(name = "T_CARD_ACCOUNT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="card_account_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"cardAccountId"})
@ToString(includeFieldNames = true, exclude = {})
@Getter
@Setter
public class CardAccount implements Serializable {
    private static final long serialVersionUID = -1059619797439373147L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "card_account_gen")
    @Column(name = "card_account_id")
    private Long cardAccountId;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "card_pin")
    private Integer cardPin;

    @Column(name = "in_transaction")
    private Boolean inTransaction;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cardAccount", orphanRemoval = true)
    private Set<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (inTransaction == null) {
            inTransaction = false;
        }
    }
}
