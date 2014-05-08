package de.rwth.idsg.velocity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import de.rwth.idsg.velocity.domain.util.CustomLocalDateSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@DiscriminatorValue("customer")
@Table(name="T_CUSTOMER")
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Integer getCardPin() {
        return cardPin;
    }

    public void setCardPin(Integer cardPin) {
        this.cardPin = cardPin;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

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
