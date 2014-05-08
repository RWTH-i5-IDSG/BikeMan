package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@DiscriminatorValue("customer")
public class Customer extends User implements Serializable {


    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "mail_address")
    private String mailAddress;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "card_pin")
    private Integer cardPin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "customer", orphanRemoval = true)
    @JsonManagedReference("customer_transactions")
    private Set<Transaction> transactions;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Customer customer = (Customer) o;

        return customer.equals(o);
    }

//    @Override
//    public int hashCode() {
//        return (int) (id ^ (id >>> 32));
//    }

//    @Override
//    public String toString() {
//        return "Customer{" +
//                "id=" + id +
////                ", sampleTextAttribute='" + sampleTextAttribute + '\'' +
////                ", sampleDateAttribute=" + sampleDateAttribute +
//                '}';
//    }
}
