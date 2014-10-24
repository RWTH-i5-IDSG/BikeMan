package de.rwth.idsg.bikeman.domain;

import de.rwth.idsg.bikeman.domain.login.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by swam on 09/10/14.
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = false, of = {"name"})
@ToString(exclude = {"cardAccounts"})
@DiscriminatorValue("majorCustomer")
@Table(name="T_MAJOR_CUSTOMER")
public class MajorCustomer extends User {
    private static final long serialVersionUID = 1724868961860110834L;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<CardAccount> cardAccounts;

}