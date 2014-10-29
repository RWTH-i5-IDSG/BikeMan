package de.rwth.idsg.bikeman.domain;

import de.rwth.idsg.bikeman.domain.login.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("customer")
@Table(name="T_CUSTOMER",
        indexes = {
                @Index(columnList="address_id", unique = true),
                @Index(columnList="customer_id", unique = true)})
@EqualsAndHashCode(callSuper = false, of = {"customerId"})
@ToString(includeFieldNames = true, exclude = {"address", "transactions", "cardAccount"})
@Getter
@Setter
public class Customer extends User {
    private static final long serialVersionUID = -9218087801102094634L;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "in_transaction")
    private Boolean inTransaction;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    private Set<Transaction> transactions;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private CardAccount cardAccount;

    @PrePersist
    public void prePersist() {
        super.prePersist();

        if (inTransaction == null) {
            inTransaction = false;
        }

        if (isActivated == null) {
            isActivated = false;
        }
    }
}
