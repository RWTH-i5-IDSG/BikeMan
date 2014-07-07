package de.rwth.idsg.velocity.domain;

import de.rwth.idsg.velocity.domain.login.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@DiscriminatorValue("customer")
@Table(name="T_CUSTOMER",
        indexes = {
                @Index(columnList="address_id", unique = true),
                @Index(columnList="customer_id", unique = true)})
@EqualsAndHashCode(of = {"customerId"}, callSuper = false)
@ToString(includeFieldNames = true)
public class Customer extends User implements Serializable {

    @Column(name = "customer_id")
    @Getter @Setter
    private String customerId;

    @Column(name = "card_id")
    @Getter @Setter
    private String cardId;

    @Column(name = "first_name")
    @Getter @Setter
    private String firstname;

    @Column(name = "last_name")
    @Getter @Setter
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @Getter @Setter
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "birthday")
    @Getter @Setter
    private LocalDate birthday;

    @Column(name = "is_activated")
    @Getter @Setter
    private Boolean isActivated;

    @Column(name = "in_transaction")
    @Getter @Setter
    private Boolean inTransaction;

    @Column(name = "card_pin")
    @Getter @Setter
    private Integer cardPin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    @Getter @Setter
    private Set<Transaction> transactions;

    @PrePersist
    public void prePersist() {
        if (inTransaction == null) {
            inTransaction = false;
        }

        if (isActivated == null) {
            isActivated = false;
        }
    }

}
