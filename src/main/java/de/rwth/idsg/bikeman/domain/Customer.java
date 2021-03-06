package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

@Entity
@DiscriminatorValue("customer")
@Table(name="T_CUSTOMER",
        indexes = {
                @Index(columnList="address_id", unique = true),
                @Index(columnList="customer_id", unique = true)})
@EqualsAndHashCode(callSuper = false, of = {"customerId"})
@ToString(includeFieldNames = true, exclude = {"address", "cardAccount", "activationKeys"})
@XmlAccessorType(XmlAccessType.NONE)
@Getter
@Setter
public class Customer extends User {
    private static final long serialVersionUID = -9218087801102094634L;

    @Column(name = "customer_id")
    private String customerId;

    @XmlElement(name = "firstname")
    @Column(name = "first_name")
    private String firstname;

    @XmlElement(name = "lastname")
    @Column(name = "last_name")
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "is_activated")
    private Boolean isActivated = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private CardAccount cardAccount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    private Set<ActivationKey> activationKeys;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }
}
