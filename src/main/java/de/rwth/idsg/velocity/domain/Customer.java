package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import de.rwth.idsg.velocity.domain.login.User;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateSerializer;
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
@Table(name="T_CUSTOMER")
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @Getter @Setter
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "birthday")
    @Getter @Setter
    private LocalDate birthday;

    @Column(name = "mail_address")
    @Getter @Setter
    private String mailAddress;

    @Column(name = "is_activated")
    @Getter @Setter
    private Boolean isActivated;

    @Column(name = "in_transaction")
    @Getter @Setter
    private Boolean inTransaction;

    @Column(name = "card_pin")
    @Getter @Setter
    private Integer cardPin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer", orphanRemoval = true)
    @JsonManagedReference("customer_transactions")
    @Getter @Setter
    private Set<Transaction> transactions;

}
