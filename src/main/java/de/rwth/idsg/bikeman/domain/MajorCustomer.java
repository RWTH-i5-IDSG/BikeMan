package de.rwth.idsg.bikeman.domain;

import de.rwth.idsg.bikeman.domain.login.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by swam on 09/10/14.
 */

@Entity
@DiscriminatorValue("majorCustomer")
@Table(name="T_MAJOR_CUSTOMER")
public class MajorCustomer extends Manager {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<CardAccount> cardAccounts;

}